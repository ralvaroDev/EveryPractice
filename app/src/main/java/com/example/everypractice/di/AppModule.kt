package com.example.everypractice.di

import android.content.*
import androidx.datastore.core.*
import androidx.datastore.core.handlers.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.*
import com.google.firebase.ktx.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import kotlinx.coroutines.*
import javax.inject.*

private const val USER_PREFERENCES = "every_prefs"

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}