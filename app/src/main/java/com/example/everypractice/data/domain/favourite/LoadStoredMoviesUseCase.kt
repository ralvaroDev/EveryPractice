package com.example.everypractice.data.domain.favourite

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadStoredMoviesUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<PermanentFavouriteMovies>>(dispatcher){
    override fun execute(parameters: Unit): Flow<Result<List<PermanentFavouriteMovies>>> {
        return dbFavouriteRepository.obtainListOfFavouriteStoredMovies.map {
            Success(it)
        }
    }
}