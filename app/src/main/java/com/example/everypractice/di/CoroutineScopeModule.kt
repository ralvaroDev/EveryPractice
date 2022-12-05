package com.example.everypractice.di

import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import kotlinx.coroutines.*
import javax.inject.*



@InstallIn(SingletonComponent::class)
@Module
object CoroutineScopeModule {

    @Singleton
    @Provides
    fun provideCoroutineScope(@IoDispatcher dispatcher: CoroutineDispatcher): CoroutineScope{
        return CoroutineScope(SupervisorJob() + dispatcher)
    }
}