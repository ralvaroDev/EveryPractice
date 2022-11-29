package com.example.everypractice.data.red.network.models

import com.example.everypractice.consval.BASE_BACKGROUND_IMAGE_URL
import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.example.everypractice.data.domain.TemporarySearchMovie
import com.example.everypractice.data.domain.TemporarySearchMovieElement
import com.google.gson.annotations.SerializedName

data class NetworkSearchMoviesContainer(
    var page: Int? = null,
    var results: ArrayList<NetworkSearchMoviesElementContainer> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null,
)

data class NetworkSearchMoviesElementContainer(
    var adult: Boolean = false,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    var id: Int,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    var overview: String = "No overview",
    var popularity: Double = 0.0,
    @SerializedName("poster_path") var posterPath: String = "",
    @SerializedName("release_date") var releaseDate: String? = "",
    var title: String = "No TITLE",
    var video: Boolean = false,
    @SerializedName("vote_average") var voteAverage: Double = 0.0,
    @SerializedName("vote_count") var voteCount: Int = 0,
) {
    val posterPathUrl: String
        get() = BASE_POSTER_IMAGE_URL.plus(posterPath)

    val backdropPathUrl: String
        get() = BASE_BACKGROUND_IMAGE_URL.plus(backdropPath)
}

//no necesito que el elemento tenga su unitario, porque es una lista en si
fun NetworkSearchMoviesContainer.asTemporaryDomainModel(): TemporarySearchMovie {
    return TemporarySearchMovie(
        page = page,
        results = results.asInsideTemporaryDomainModel(),
        totalPages = totalPages!!,
        totalResults = totalResults!!
    )
}

/**
 * Revisar que sea necesario una lista y no un solo elemento, en dicho caso usa el elemento
 * */
fun List<NetworkSearchMoviesContainer>.asTemporaryDomainModel(): List<TemporarySearchMovie> {
    return map {
        TemporarySearchMovie(
            page = it.page!!,
            results = it.results.asInsideTemporaryDomainModel(),
            totalPages = it.totalPages!!,
            totalResults = it.totalResults!!
        )
    }
}

fun List<NetworkSearchMoviesElementContainer>.asInsideTemporaryDomainModel(): List<TemporarySearchMovieElement> {
    return map {
        TemporarySearchMovieElement(
            adult = it.adult,
            backdropPathUrl = it.backdropPathUrl,
            genreIds = it.genreIds.toList(),
            id = it.id,
            overview = it.overview,
            popularity = it.popularity,
            posterPathUrl = it.posterPathUrl,
            releaseDate = it.releaseDate,
            title = it.title,
            video = it.video,
            voteAverage = it.voteAverage
        )
    }
}


