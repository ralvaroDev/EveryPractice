package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadMovieImagesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    suspend fun downloadImagesOfMovieFromServer(id: Int, language: String): StateFlow<ImageSource> {
        return flow {
            try {
                val response = withContext(dispatcher) {
                    return@withContext networkRepository.obtainImagesESFromMovieWithGivenId(
                        id,
                        language
                    )
                }
                emit(ImageSource(DONE, response))
            } catch (e: Exception) {
                emit(ImageSource(state = ERROR, exception = e))
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), ImageSource())
    }

}

data class ImageSource(
    val state: RequestNetStatus = LOADING,
    val data: TemporaryImageMovie = TemporaryImageMovie(),
    val exception: Exception? = null
)