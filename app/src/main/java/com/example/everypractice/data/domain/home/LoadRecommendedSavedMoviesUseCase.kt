package com.example.everypractice.data.domain.home

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadRecommendedSavedMoviesUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<PermanentFavouriteMovies>>(dispatcher){
    override fun execute(parameters: Unit): Flow<Result<List<PermanentFavouriteMovies>>> {
        return dbFavouriteRepository.randomFOURFavouriteMoviesFromDatabase.map {
            Success(it)
        }
    }
}

