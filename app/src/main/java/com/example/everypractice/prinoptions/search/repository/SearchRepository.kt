package com.example.everypractice.prinoptions.search.repository

import androidx.annotation.WorkerThread
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.data.LastSearchDao
import kotlinx.coroutines.flow.Flow

//ESTE NOS DA EL ACCESO A LAS FUNCIONES DE MODIFICACION DE LA BASE DE DATOS, SINE XPONER TODO LA BASE DDE DATOS
class SearchRepository(private val lastSearchDao: LastSearchDao) {

    val allHistory: Flow<List<LastSearch>> = lastSearchDao.getLastSearchList()

    fun obtainLastSearchById(id: Int): Flow<LastSearch> {
        return lastSearchDao.getLastSearchById(id)
    }

    fun obtainIdByWord(lastSearch: String): Int {
        return lastSearchDao.getIdByWord(lastSearch)
    }

    fun obtainListWords() = lastSearchDao.getWordsOfLastSearchList()

    fun deleteLastSearchByWord(word: String) = lastSearchDao.deleteByWord(word)

    fun updateLastSearchTimestamp(word: String, timestamp: Long) = lastSearchDao.updateLastSearchTimestamp(word, timestamp)

    @WorkerThread
    suspend fun insert(lastSearch: LastSearch) {
        lastSearchDao.insert(lastSearch)
    }

    @WorkerThread
    suspend fun delete(lastSearch: LastSearch) {
        lastSearchDao.delete(lastSearch)
    }

}