package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadMovieStaffUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    suspend fun downloadStaffOfMovieFromServer(id: Int): StateFlow<StaffSource>{
        return flow {
            try {
                val response = withContext(dispatcher){
                    return@withContext networkRepository.obtainStaffOfAMovieWithId(id)
                }
                emit(StaffSource(DONE, response))
            } catch (e: Exception){
                emit(StaffSource(state = ERROR, exception = e))
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), StaffSource())
    }

}

data class StaffSource(
    val state: RequestNetStatus = LOADING,
    val data: List<TemporaryStaffMovie> = emptyList(),
    val exception: Exception? = null
)