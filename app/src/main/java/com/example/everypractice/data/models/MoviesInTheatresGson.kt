package com.example.everypractice.data.models

import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.google.gson.annotations.SerializedName

data class MoviesInTheatresGson(
    val dates: Dates,
    val page: Int,
    val results: ArrayList<Results>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class Dates(
    val maximum: String, val minimum: String
)

data class Results(
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("genre_ids") val genreIds: ArrayList<Int>,
    val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {
    val posterUrl: String
        get() = BASE_POSTER_IMAGE_URL.plus(posterPath)
}
