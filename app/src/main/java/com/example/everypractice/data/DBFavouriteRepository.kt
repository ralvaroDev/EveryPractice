package com.example.everypractice.data

import androidx.annotation.*
import com.example.everypractice.data.database.*
import com.example.everypractice.data.models.*
import kotlinx.coroutines.flow.*
import javax.inject.*

//TODO ESTE DEBE SER SINGLETOS?
class DBFavouriteRepository @Inject constructor(
    private val daoDatabaseFavouriteMovie: DaoDatabaseFavouriteMovie
) {

    /*----DATA BASE----*/

    val obtainListOfFavouriteStoredMovies: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(true).map {
            it.asDomainModel()
        }

    val obtainListOfFavouriteSeenMovies: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(false).map {
            it.asDomainModel()
        }

    val randomFOURFavouriteMoviesFromDatabase: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(true).map {
            it.asDomainModel().shuffled().take(4)
        }

    val fullListOfIdsStoredInDatabase: Flow<List<Int>> =
        daoDatabaseFavouriteMovie.getListOfIdStoredInDatabase()

    fun obtainFavouriteMovieByIdFromDatabase(id: Int): Flow<DatabaseFavouriteMovie> {
        return daoDatabaseFavouriteMovie.getDatabaseFavouriteMovieById(id)
    }

    @WorkerThread
    suspend fun insert(
        temporaryDetailMovie: TemporaryDetailMovie,
        timestamp: Long,
    ) {
        daoDatabaseFavouriteMovie.insert(temporaryDetailMovie.asDatabaseModel(timestamp))
    }

    @WorkerThread
    suspend fun delete(
        permanentFavouriteMovies: PermanentFavouriteMovies,
    ) {
        daoDatabaseFavouriteMovie.delete(permanentFavouriteMovies.asDatabaseModel())
    }

    @WorkerThread
    suspend fun customDeleteWithId(
        id: Int,
    ) {
        daoDatabaseFavouriteMovie.customDeleteWithIdFromDetails(id)
    }

    @WorkerThread
    suspend fun customUpdateWithIdAndNewStateSaved(
        saved: Boolean, id: Int
    ) {
        daoDatabaseFavouriteMovie.updateSaveStateInDatabase(saved, id)
    }

}