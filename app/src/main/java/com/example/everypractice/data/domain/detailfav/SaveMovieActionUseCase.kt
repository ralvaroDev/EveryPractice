package com.example.everypractice.data.domain.detailfav

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import javax.inject.*

class SaveMovieActionUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<SaveSource, Unit>(dispatcher) {
    override suspend fun execute(parameters: SaveSource) {
        val (movie, time) = parameters
        dbFavouriteRepository.insert(movie,time)
    }

}

data class SaveSource(
    val movie: TemporaryDetailMovie,
    val timestamp: Long
)