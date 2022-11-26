package com.example.everypractice.start.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.everypractice.start.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_ONBOARDING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferenceStorage {

    suspend fun completeOnboarding(complete: Boolean)
    val onboardingCompleted: Flow<Boolean>


}


object PreferenceStorageModule {

    val Context.dataStore by preferencesDataStore(
        name = DataStorePreferenceStorage.PREFS_NAME,
        produceMigrations = { context ->
            listOf(
                SharedPreferencesMigration(
                    context,
                    DataStorePreferenceStorage.PREFS_NAME
                )
            )
        }
    )

    fun providePreferenceStorage(context: Context): PreferenceStorage =
        DataStorePreferenceStorage(context.dataStore)

}


class DataStorePreferenceStorage (
    private val dataStore: DataStore<Preferences>
): PreferenceStorage {

    companion object {
        const val PREFS_NAME = "sunoff"
    }

    object PreferencesKeys {

        val PREF_ONBOARDING = booleanPreferencesKey("pref_onboarding")
    }

    //SET
    override suspend fun completeOnboarding(complete: Boolean) {
        dataStore.edit {
            it[PREF_ONBOARDING] = complete
        }
    }
    //GET
    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[PREF_ONBOARDING] ?: false }


}