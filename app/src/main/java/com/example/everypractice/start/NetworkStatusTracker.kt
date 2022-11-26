package com.example.everypractice.start

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

sealed class NetworkStatus{
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

class NetworkStatusTracker(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatus = callbackFlow<NetworkStatus> {

        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable)
            }
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }
            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }
        .distinctUntilChanged()
}

inline fun <Result> Flow<NetworkStatus>.map(
    crossinline onUnavailable: suspend () -> Result,
    crossinline onAvailable: suspend () -> Result,
): Flow<Result> = map { networkStatus->
    when (networkStatus){
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}

inline fun <Result> Flow<NetworkStatus>.flatMap(
    crossinline onUnavailable: suspend () -> Flow<Result>,
    crossinline onAvailable: suspend () -> Flow<Result>,
): Flow<Result> = flatMapConcat { status ->
    when(status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}