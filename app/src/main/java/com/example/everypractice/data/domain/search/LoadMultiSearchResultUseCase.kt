package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.red.network.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadMultiSearchResultUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    suspend fun downloadResultsOfMultiSearchFromServer(name: String): StateFlow<MultiSearchResultSource> {
        return flow {
            try {
                val response = withContext(dispatcher) {
                    return@withContext networkRepository.obtainMultiSEARCHWithWord(name)
                }
                emit(MultiSearchResultSource(DONE, response))
            } catch (e:Exception){
                emit(MultiSearchResultSource(state = ERROR, exception = e))
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), MultiSearchResultSource())
    }

}

data class MultiSearchResultSource(
    val state : RequestNetStatus = LOADING,
    val data: NetworkMultiSearchContainer = NetworkMultiSearchContainer(),
    val exception: Exception? = null
)