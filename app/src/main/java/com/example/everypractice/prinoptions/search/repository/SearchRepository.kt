package com.example.everypractice.prinoptions.search.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.data.LastSearchDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//ESTE NOS DA EL ACCESO A LAS FUNCIONES DE MODIFICACION DE LA BASE DE DATOS, SINE XPONER TODO LA BASE DDE DATOS
class SearchRepository(private val lastSearchDao: LastSearchDao) {

    val allHistory: Flow<List<LastSearch>> = lastSearchDao.getHistorial()

    fun obtainOneHistorial(id: Int): Flow<LastSearch> {
        return lastSearchDao.getOneHistorial(id)
    }

    fun obtainIdAlreadySearch(lastSearch: String): Int {
        return lastSearchDao.getIdAlreadySearch(lastSearch)
    }

    fun obtainListWords() = lastSearchDao.getListWordsHistorial()

    @WorkerThread
    suspend fun insert(lastSearch: LastSearch) {
        lastSearchDao.insert(lastSearch)
    }

    @WorkerThread
    suspend fun delete(lastSearch: LastSearch) {
        lastSearchDao.delete(lastSearch)
    }

}