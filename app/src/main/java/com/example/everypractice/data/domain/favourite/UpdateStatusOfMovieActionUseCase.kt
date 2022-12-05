package com.example.everypractice.data.domain.favourite

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import javax.inject.*

class UpdateStatusOfMovieActionUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<UpdateSource, Unit>(dispatcher) {

    override suspend fun execute(parameters: UpdateSource) {
        val (saved, id) = parameters
        dbFavouriteRepository.customUpdateWithIdAndNewStateSaved(saved, id)
    }
}

data class UpdateSource(
    val saved: Boolean,
    val id: Int
)