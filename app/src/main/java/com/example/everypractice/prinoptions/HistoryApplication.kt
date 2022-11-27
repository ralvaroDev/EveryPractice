package com.example.everypractice.prinoptions

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.everypractice.prinoptions.movies.data.database.DatabaseDBFavouriteMovie
import com.example.everypractice.prinoptions.movies.repository.FavouriteMovieRepository
import com.example.everypractice.prinoptions.search.data.SearchDatabase
import com.example.everypractice.prinoptions.search.repository.SearchRepository
import com.example.everypractice.start.datastore.UserPreferenceRepository
import timber.log.Timber

class HistoryApplication : Application() {

    //APP SEARCH
    private val database : SearchDatabase by lazy { SearchDatabase.getDatabase(this) }
    val repository : SearchRepository by lazy { SearchRepository(database.lastSearchDao) }


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