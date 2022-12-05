package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadPopularMoviesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    fun something(): StateFlow<PopularSource> {

        return flow {
            try {
                val response1 = withContext(dispatcher) {
                    return@withContext networkRepository.obtainListOfPopularMovies()
                }
                emit(PopularSource(DONE, response1))
            } catch (e: Exception) {
                emit(PopularSource(ERROR, emptyList(), e))
            }
            //TODO, AQUI PROBAR LOS OTROS TIPOS DE LANZADO, LAZILY ETC
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), PopularSource())

    }
}

data class PopularSource(
    val state: RequestNetStatus = LOADING,
    val data: List<TemporaryPopularMovieElement> = emptyList(),
    val exception: Exception? = null
)
