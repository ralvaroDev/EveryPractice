package com.example.everypractice.data.domain.detailfav

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.di.*
import kotlinx.coroutines.*
import javax.inject.*

class DeleteMovieActionUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, Unit>(dispatcher) {
    override suspend fun execute(parameters: Int) {
        dbFavouriteRepository.customDeleteWithId(parameters)
    }
}