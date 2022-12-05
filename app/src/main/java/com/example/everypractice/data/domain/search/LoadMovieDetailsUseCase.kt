package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadMovieDetailsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    suspend fun downLoadDetailOfMovieFromServer(id: Int): StateFlow<DetailSource> {
        return flow {
            try {
                val response = withContext(dispatcher) {
                    return@withContext networkRepository.obtainDetailFromMovieWithId(id)
                }
                emit(DetailSource(DONE, response))
            } catch (e: Exception) {
                emit(DetailSource(ERROR, exception = e))
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), DetailSource())
    }

}

data class DetailSource(
    val state: RequestNetStatus = LOADING,
    val data: TemporaryDetailMovie = TemporaryDetailMovie(),
    val exception: Exception? = null
)