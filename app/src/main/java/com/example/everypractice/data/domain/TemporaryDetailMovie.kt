package com.example.everypractice.data.domain

import com.example.everypractice.data.database.DatabaseFavouriteMovie

data class TemporaryDetailMovie(
    val id: Int = 0,
    val movieTitle: String = "No title",
    val overview: String = "",
    val adult: Boolean = false,
    val genres: List<Int> = listOf(),
    //TODO LUEGO VER SI ES RECOMENDABLE TENER DIRECTO AQUI EL BITMAP O SOLO LA URL
    //TODO EL QUE TENGAN VAL HACE QUE NUNCA VAYAN A ACTUALIZAR?
    val posterPathUrl: String = "",
    val backdropPathUrl: String = "",
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val idCollection: Int = 0,
    val backgroundCollectionPathUrl: String = "",
    val nameCollectionBelongsTo: String = "No name collection",
    val runtime: Int = 0,
) {
    val genreName: List<String>
        get() {
            return genres.map {
                it.getGenreNameFromId()
            }
        }

    private fun Int.getGenreNameFromId(): String {
        return when (this) {
            28 -> "Action"
            12 -> "Action"
            else -> "NN"
        }
    }
}

fun TemporaryDetailMovie.asDatabaseModel(timestampAdd: Long): DatabaseFavouriteMovie {
    return DatabaseFavouriteMovie(
        id = id,
        movieTitle = movieTitle,
        overview = overview,
        adult = adult,
        generes = genres,
        //it es un string y poster un bitmap. Aqui en caso explote usar la func get bitmap
        //creo que explotara por el suspend
        posterPath = posterPathUrl,
        //aqui usamos la func de una funcion
        backdropPath = backdropPathUrl,
        releaseDate = releaseDate,
        vote_Average = voteAverage,
        //TODO AQUI CUANDO SE CREA EL LAYOUT QUE LEE, DEBE SER EN BASE A LAS RESPUESTAS, SI ES ERROR QUE NO APAREZCA
        idCollection = idCollection,
        backgroundCollectionPath = backgroundCollectionPathUrl,
        nameCollectionBelongsTo = nameCollectionBelongsTo,
        runtime = runtime,
        timestampAdd = timestampAdd,
        saved = true
    )
}

fun List<TemporaryDetailMovie>.asDatabaseModel(timestampAdd: Long): List<DatabaseFavouriteMovie>{
    return map {
        DatabaseFavouriteMovie(
            id = it.id,
            movieTitle = it.movieTitle,
            overview = it.overview,
            adult = it.adult,
            generes = it.genres,
            //it es un string y poster un bitmap. Aqui en caso explote usar la func get bitmap
            //creo que explotara por el suspend
            posterPath = it.posterPathUrl,
            //aqui usamos la func de una funcion
            backdropPath = it.backdropPathUrl,
            releaseDate = it.releaseDate,
            vote_Average = it.voteAverage,
            //TODO AQUI CUANDO SE CREA EL LAYOUT QUE LEE, DEBE SER EN BASE A LAS RESPUESTAS, SI ES ERROR QUE NO APAREZCA
            idCollection = it.idCollection,
            backgroundCollectionPath = it.backgroundCollectionPathUrl,
            nameCollectionBelongsTo = it.nameCollectionBelongsTo,
            runtime = it.runtime,
            timestampAdd = timestampAdd,
            saved = true
        )
    }
}
