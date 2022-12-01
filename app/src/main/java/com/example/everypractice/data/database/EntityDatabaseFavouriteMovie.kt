package com.example.everypractice.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.everypractice.data.models.PermanentFavouriteMovies

/**
 * Data type (Room) that is gonna be saved in device, it actual support Lists, Bitmap too.
 */
@Entity(tableName = "data_favourite_movies")
data class DatabaseFavouriteMovie(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "movie_title") val movieTitle: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "generes") val generes: List<Int>,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    //IMAGEN DE FONDO
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "vote_average") val vote_Average: Double,
    @ColumnInfo(name = "id_collection") val idCollection: Int,
    @ColumnInfo(name = "background_collection_path") val backgroundCollectionPath: String,
    @ColumnInfo(name = "name_collection_belongs_to") val nameCollectionBelongsTo: String,
    @ColumnInfo(name = "runtime") val runtime: Int,
    @ColumnInfo(name = "timestamp_add") var timestampAdd: Long,
    @ColumnInfo(name = "saved") val saved: Boolean
)

//TODO AQUI SE PUEDE USAR UN CONVERTER DIRECTO Y ALLA HACER LA CONVERSION DE CADA DISTINTO TIPO HACIA AQUI
/**
* Map  DatabaseFavouriteMovies to domain entities in order to operate with this
*/
fun List<DatabaseFavouriteMovie>.asDomainModel(): List<PermanentFavouriteMovies>{
    return map {
        PermanentFavouriteMovies(
            id = it.id,
            movieTitle = it.movieTitle,
            overview = it.overview,
            adult = it.adult,
            genres = it.generes,
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            releaseDate = it.releaseDate,
            vote_Average = it.vote_Average,
            idCollection = it.idCollection,
            backgroundCollectionPath = it.backgroundCollectionPath,
            nameCollectionBelongsTo = it.nameCollectionBelongsTo,
            runtime = it.runtime,
            timestampAdd = it.timestampAdd,
            saved = it.saved
        )
    }
}
