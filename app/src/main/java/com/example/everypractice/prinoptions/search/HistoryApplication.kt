package com.example.everypractice.prinoptions.search

import android.app.Application
import com.example.everypractice.prinoptions.search.data.SearchDatabase
import com.example.everypractice.prinoptions.search.repository.SearchRepository

class HistoryApplication : Application() {

    val database : SearchDatabase by lazy { SearchDatabase.getDatabase(this) }

    val repository : SearchRepository by lazy { SearchRepository(database.lastSearchDao()) }

}