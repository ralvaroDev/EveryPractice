package com.example.everypractice.ui

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.everypractice.ui.signin.datastore.UserPreferenceRepository
import com.example.everypractice.data.database.DatabaseDBFavouriteMovie
import com.example.everypractice.data.FavouriteMovieRepository
import timber.log.Timber

class MainApplication : Application() {

    //APP MOVIE
    private val movieDatabase : DatabaseDBFavouriteMovie by lazy { DatabaseDBFavouriteMovie.getDatabase(this) }
    val movieRepository : FavouriteMovieRepository by lazy { FavouriteMovieRepository(movieDatabase.DaoFavouriteMovies()) }

    private val USER_PREFERENCE_NAME = "user_preferences"
    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCE_NAME
    )
    val userPreferenceRepository : UserPreferenceRepository by lazy { UserPreferenceRepository(dataStore) }








    override fun onCreate() {
        super.onCreate()
        Timber.plant(MyTree())
    }

}

class MyTree: Timber.DebugTree(){
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "$tag ARC", message, t)
    }
}