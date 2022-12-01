package com.example.everypractice.ui.signin.datastore

import androidx.datastore.core.*
import androidx.datastore.preferences.core.*
import com.example.everypractice.ui.*
import kotlinx.coroutines.flow.*
import javax.inject.*

/**
 * DataClass con las preferencias
 */
data class UserPreference(
    val userLastSession: Boolean,
    val nameUser: String,
    val emailUser: String
)

class UserPreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Este object almacena el valor de las llaves de DataStore
     */
    private object PreferencesKeys {
        val INITIAL_STATE = intPreferencesKey("initial_view")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_USER_LOGED = booleanPreferencesKey("is_user_loged")
        val NAME_USER = stringPreferencesKey("name_user")
        val EMAIL_USER = stringPreferencesKey("email_user")
    }

    suspend fun initialState(initialView: StartView) {
        dataStore.edit {
            it[PreferencesKeys.INITIAL_STATE] = initialView.ordinal
        }
    }
    val initialState: Flow<Int> =
        dataStore.data.map { it[PreferencesKeys.INITIAL_STATE] ?: StartView.LOGIN.ordinal }

    suspend fun completeOnboarding(complete: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.ONBOARDING_COMPLETED] = complete
        }
    }
    val completeOnboarding: Flow<Boolean> =
        dataStore.data.map { it[PreferencesKeys.ONBOARDING_COMPLETED] ?: false }


    /**
     * Este flow emite los valores almacenados, en caso no haya ninguno envia los valores por defecto
     */
    val userPreferenceFlow: Flow<UserPreference> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        //el mapeo es necesario para colocar los primeros valores por defecto en caso se llame antes de setear uno a peticion
        .map {
            val userLastSession = it[PreferencesKeys.IS_USER_LOGED] ?: false
            val nameUser = it[PreferencesKeys.NAME_USER] ?: "No name"
            val emailUser = it[PreferencesKeys.EMAIL_USER] ?: "No email"
            UserPreference(userLastSession, nameUser, emailUser)
        }

    suspend fun updateUserLastSession(exist: Boolean, nameUser: String, emailUser: String) {
        dataStore.edit { preferences ->
            val currentSession = preferences[PreferencesKeys.IS_USER_LOGED] ?: false
            if (currentSession && exist) {
                //TODO HACER FUNCION DE REVISAR SI CONTINUA LA SESION
            } else if (currentSession && !exist) {
                preferences[PreferencesKeys.NAME_USER] = nameUser
                preferences[PreferencesKeys.EMAIL_USER] = emailUser
            } else if (!currentSession && exist) {
                preferences[PreferencesKeys.NAME_USER] = nameUser
                preferences[PreferencesKeys.EMAIL_USER] = emailUser
            } else {
            }
            preferences[PreferencesKeys.IS_USER_LOGED] = exist
        }
    }

    //to only update the name
    suspend fun saveNameUser(nameUser: String) {
        dataStore.edit {
            it[PreferencesKeys.NAME_USER] = nameUser
        }
    }

    //to only update the email
    suspend fun saveEmailUser(emailUser: String) {
        dataStore.edit {
            it[PreferencesKeys.EMAIL_USER] = emailUser
        }
    }
}
