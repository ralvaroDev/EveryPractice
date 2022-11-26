package com.example.everypractice.prinoptions.search.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LastSearch::class], version = 1, exportSchema = false)
abstract class SearchDatabase: RoomDatabase(){

    abstract val lastSearchDao : LastSearchDao

    companion object{

        @Volatile
        private var INSTANCE: SearchDatabase? = null

        fun getDatabase(context: Context): SearchDatabase {

            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    "history_search"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                return instance

            }

        }
    }

}