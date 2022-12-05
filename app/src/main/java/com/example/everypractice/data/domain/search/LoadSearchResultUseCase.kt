package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadSearchResultUseCase  @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    suspend fun downloadResultsOfSearchFromServer(name: String): StateFlow<SearchResultSource> {
        return flow {
            try {
                val response = withContext(dispatcher) {
                    return@withContext networkRepository.obtainListOfMoviesFromSearchWithWord(name)
                }
                emit(SearchResultSource(DONE, response))
            } catch (e: Exception) {
                emit(SearchResultSource(ERROR, exception = e))
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), SearchResultSource())
    }

}

data class SearchResultSource(
    val state: RequestNetStatus = LOADING,
    val data: TemporarySearchMovie = TemporarySearchMovie(),
    val exception: Exception? = null
)