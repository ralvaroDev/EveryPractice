package com.example.everypractice.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [DatabaseFavouriteMovie::class]
, version = 1
, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseDBFavouriteMovie : RoomDatabase() {

    abstract fun DaoFavouriteMovies(): DaoDatabaseFavouriteMovie

    /*esto mantiene referencia constante a la base de datos cuando se cree una
    debe ser en companion para que solo exista una*/
    companion object{

        @Volatile   //esto es para que instance siempre este actualizado
        private var INSTANCE: DatabaseDBFavouriteMovie? = null

        fun getDatabase(context: Context): DatabaseDBFavouriteMovie {

            /*esto es para evitar que se hagan muchas solicitudes de base de datos
            si existe regresa este mismo, si no, se inicializa con el syncro*/
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseDBFavouriteMovie::class.java,
                    "data_favourite_movies"
                )
                    /*este fall back es un proceso de migracion, normalmente se deberia de poner
                    en caso cambiemos a otro esquema de datos y estos no se pierdan
                    en este caso solo diremos que se destruyan y se creen de nuevo*/
                    .fallbackToDestructiveMigration()
                    .build()

                //con esto aseguramos que en caso se haya creado ya no vuelva a crear desnulizandolo
                INSTANCE = instance

                return instance
            }
        }
    }
}