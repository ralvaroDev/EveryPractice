package com.example.everypractice

import android.os.*
import android.widget.*
import com.google.firebase.messaging.*
import timber.log.*

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Looper.prepare()
        Handler(mainLooper).post{
            Toast.makeText(baseContext, message.notification?.title, Toast.LENGTH_LONG).show()
        }
        Looper.loop()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("NewToke registered: $token")
    }

}