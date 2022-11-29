package com.example.everypractice.data.domain

import com.example.everypractice.utils.util.smartTruncate
import com.example.everypractice.data.database.DatabaseFavouriteMovie


/**
 * Data type that we are gonna used inside the app to operate
 */
data class PermanentFavouriteMovies(
    val id: Int,
    val movieTitle: String,
    val overview: String,
    val adult: Boolean,
    val genres: List<Int>,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val vote_Average: Double,
    val idCollection: Int,
    val backgroundCollectionPath: String,
    val nameCollectionBelongsTo: String,
    val runtime: Int,
    var timestampAdd: Long,
    val saved: Boolean
){

    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
    val shortOverview: String
        get() =  overview.smartTruncate(200)
}

fun PermanentFavouriteMovies.asDatabaseModel(): DatabaseFavouriteMovie {
    return DatabaseFavouriteMovie(
        id = id,
        movieTitle = movieTitle,
        overview = overview,
        adult = adult,
        generes = genres,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        vote_Average = vote_Average,
        idCollection = idCollection,
        backgroundCollectionPath = backgroundCollectionPath,
        nameCollectionBelongsTo = nameCollectionBelongsTo,
        runtime = runtime,
        timestampAdd = timestampAdd,
        saved = saved
    )
}