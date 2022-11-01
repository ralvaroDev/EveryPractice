package com.example.everypractice.prinoptions.search.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

@Dao
interface LastSearchDao {

    @Query("SELECT * from history_search ORDER BY timestamp DESC LIMIT 10")
    fun getLastSearchList(): Flow<List<LastSearch>>

    @Query("SELECT last_search from history_search")
    fun getWordsOfLastSearchList(): List<String>

    @Query("SELECT * from history_search WHERE id = :id")
    fun getLastSearchById(id: Int): Flow<LastSearch>

    @Query("SELECT id from history_search WHERE last_search = :lastSearch")
    fun getIdByWord(lastSearch: String): Int

    @Query("delete from history_search where last_search = :word")
    fun deleteByWord(word: String)

    @Query("update history_search set timestamp = :timestamp where last_search = :word")
    fun updateLastSearchTimestamp(word: String,timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historial: LastSearch)

    @Delete
    suspend fun delete(historial: LastSearch)

}