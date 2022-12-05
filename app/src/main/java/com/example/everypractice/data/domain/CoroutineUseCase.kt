package com.example.everypractice.data.domain

import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Error
import com.example.everypractice.utils.Result.Loading
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

abstract class CoroutineUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    Result.Success(it)
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Error(e)
        }
    }

    protected abstract suspend fun execute(parameters: P): R
}

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .catch { e ->
            emit(Error(Exception(e)))
            Timber.d("No entro aqui xd")
        }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<Result<R>>
}


//THIS HAS ERROR WITH NO CONECTION
abstract class StatusFlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(parameters: P): StateFlow<Result<R>> {
        return try {
            execute(parameters).flowOn(coroutineDispatcher).stateIn(coroutineScope,
                SharingStarted.Eagerly, Loading)
        } catch (e: Exception){
            flow { emit(Error(e)) }.flowOn(coroutineDispatcher).stateIn(coroutineScope, SharingStarted.Eagerly, Loading)
        }
    }

    protected abstract fun execute(parameters: P): Flow<Result<R>>

}

/*
* {
        return try {
            execute(parameters).flowOn(coroutineDispatcher).stateIn(coroutineScope, SharingStarted.Eagerly, Loading)

        } catch (e: Exception){
            flow { emit(Error(Exception(e))) }.flowOn(coroutineDispatcher).stateIn(coroutineScope, SharingStarted.Eagerly, Loading)
        }
    }*/

abstract class SuspendFlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .catch { e -> emit(Error(Exception(e))) }
        .flowOn(coroutineDispatcher)

    protected abstract suspend fun execute(parameters: P): Flow<Result<R>>
}