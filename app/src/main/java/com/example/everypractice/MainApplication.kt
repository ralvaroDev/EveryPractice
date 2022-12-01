package com.example.everypractice

import android.app.*
import android.content.*
import androidx.datastore.preferences.*
import com.example.everypractice.data.*
import com.example.everypractice.data.database.*
import com.example.everypractice.ui.signin.datastore.*
import dagger.hilt.android.*
import timber.log.*

@HiltAndroidApp
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
        super.log(priority, "$tag FATAL", message, t)
    }
}