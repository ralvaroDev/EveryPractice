package com.example.everypractice.data.domain.search

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class PopularMoviesActionUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : SuspendFlowUseCase<Unit, TemporaryPopularMovie>(dispatcher){
    override suspend fun execute(parameters: Unit): Flow<Result<TemporaryPopularMovie>> {
        return networkRepository.obtainListOfPopularMovies()
    }

}