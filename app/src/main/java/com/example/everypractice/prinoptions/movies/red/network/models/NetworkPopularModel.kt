package com.example.everypractice.prinoptions.movies.red.network.models

import com.example.everypractice.consval.BASE_BACKGROUND_IMAGE_URL
import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.example.everypractice.prinoptions.movies.data.domain.TemporaryPopularMovie
import com.example.everypractice.prinoptions.movies.data.domain.TemporaryPopularMovieElement
import com.google.gson.annotations.SerializedName

data class NetworkPopularMoviesContainer(
    var page: Int? = null,
    var results: ArrayList<NetworkResultsPopularElementContainer> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null,
)

data class NetworkResultsPopularElementContainer(
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

fun NetworkPopularMoviesContainer.asTemporaryDomainModel(): TemporaryPopularMovie {
    return TemporaryPopularMovie(
        page = page,
        results = results.asInsideTemporaryDomainModel(),
        totalPages = totalPages!!,
        totalResults = totalResults!!

    )
}

fun List<NetworkResultsPopularElementContainer>.asInsideTemporaryDomainModel(): List<TemporaryPopularMovieElement> {
    return map {
        TemporaryPopularMovieElement(
            backdropPathUrl = it.backdropPathUrl,
            genreIds = it.genreIds.toList(),
            id = it.id,
            overview = it.overview,
            posterPathUrl = it.posterPathUrl,
            title = it.title,
            video = it.video,
            voteAverage = it.voteAverage
        )
    }
}