package com.example.everypractice.data.domain.detailfav

import com.example.everypractice.data.*
import com.example.everypractice.data.domain.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoadListOfIdsStoredUseCase @Inject constructor(
    private val dbFavouriteRepository: DBFavouriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Int>>(dispatcher){
    override fun execute(parameters: Unit): Flow<Result<List<Int>>> {
        return dbFavouriteRepository.fullListOfIdsStoredInDatabase.map {
            Success(it)
        }
    }
}