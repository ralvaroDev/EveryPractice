package com.example.everypractice.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoDatabaseFavouriteMovie {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(databaseFavouriteMovie: DatabaseFavouriteMovie)

    @Update
    suspend fun update(databaseFavouriteMovie: DatabaseFavouriteMovie)

    @Delete
    suspend fun delete(databaseFavouriteMovie: DatabaseFavouriteMovie)

    @Query("SELECT * from data_favourite_movies WHERE id = :id")
    fun getDatabaseFavouriteMovieById(id:Int): Flow<DatabaseFavouriteMovie>

    @Query("SELECT * from data_favourite_movies WHERE saved = :saved ORDER BY timestamp_add ASC")
    fun getAllListDatabaseFavouriteMovies(saved: Boolean): Flow<List<DatabaseFavouriteMovie>>

    @Query("SELECT * from data_favourite_movies ORDER BY vote_average ASC LIMIT 5")
    fun getListDatabaseFavouriteMoviesRecommended(): Flow<List<DatabaseFavouriteMovie>>

    @Query("DELETE from data_favourite_movies WHERE id = :id")
    suspend fun customDeleteWithIdFromDetails(id:Int)

    @Query("SELECT id from data_favourite_movies")
    fun getListOfIdStoredInDatabase(): Flow<List<Int>>

    @Query("UPDATE data_favourite_movies SET saved= :saved WHERE id = :id")
    suspend fun updateSaveStateInDatabase(saved: Boolean, id: Int)

}