package com.example.everypractice.di

import android.content.*
import android.os.*
import androidx.room.*
import com.example.everypractice.data.database.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import javax.inject.*

private val NAME_MOVIES_DATABASE = "data_favourite_movies"

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DatabaseDBFavouriteMovie {
        val builder = Room.databaseBuilder(
            context,
            DatabaseDBFavouriteMovie::class.java,
            NAME_MOVIES_DATABASE
        )
            .fallbackToDestructiveMigration()

        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Provides
    fun provideDao(db: DatabaseDBFavouriteMovie) : DaoDatabaseFavouriteMovie {
        return db.DaoFavouriteMovies()
    }


}