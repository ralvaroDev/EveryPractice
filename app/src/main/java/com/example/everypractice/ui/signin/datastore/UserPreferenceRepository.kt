package com.example.everypractice.ui.signin.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * DataClass con las preferencias
 */
data class UserPreference(
    val userLastSession: Boolean,
    val nameUser: String,
    val emailUser: String
)

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Este object almacena el valor de las llaves de DataStore
     */
    private object PreferencesKeys {
        val IS_USER_LOGED = booleanPreferencesKey("is_user_loged")
        val NAME_USER = stringPreferencesKey("name_user")
        val EMAIL_USER = stringPreferencesKey("email_user")
    }

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
            UserPreference(userLastSession,nameUser,emailUser)
        }

    suspend fun updateUserLastSession(exist: Boolean,nameUser: String,emailUser: String) {
        dataStore.edit { preferences ->
            val currentSession = preferences[PreferencesKeys.IS_USER_LOGED] ?: false
            if (currentSession && exist){
                //TODO HACER FUNCION DE REVISAR SI CONTINUA LA SESION
            }
            else if (currentSession && !exist){
                preferences[PreferencesKeys.NAME_USER] = nameUser
                preferences[PreferencesKeys.EMAIL_USER] = emailUser
            }
            else if (!currentSession && exist) {
                preferences[PreferencesKeys.NAME_USER] = nameUser
                preferences[PreferencesKeys.EMAIL_USER] = emailUser
            } else {}
            preferences[PreferencesKeys.IS_USER_LOGED] = exist
        }
    }

    //to only update the name
    suspend fun saveNameUser(nameUser: String){
        dataStore.edit {
            it[PreferencesKeys.NAME_USER] = nameUser
        }
    }

    //to only update the email
    suspend fun saveEmailUser(emailUser: String){
        dataStore.edit {
            it[PreferencesKeys.EMAIL_USER] = emailUser
        }
    }
}
