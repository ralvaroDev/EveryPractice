package com.example.everypractice.prinoptions.search.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LastSearchDao {

    @Query("SELECT * from historial ORDER BY id DESC LIMIT 10")
    fun getLastSearchList(): Flow<List<LastSearch>>

    @Query("SELECT last_search from historial")
    fun getWordsOfLastSearchList(): List<String>

    @Query("SELECT * from historial WHERE id = :id")
    fun getLastSearchById(id: Int): Flow<LastSearch>

    @Query("SELECT id from historial WHERE last_search = :lastSearch")
    fun getIdByWord(lastSearch: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historial: LastSearch)

    @Delete
    suspend fun delete(historial: LastSearch)

}