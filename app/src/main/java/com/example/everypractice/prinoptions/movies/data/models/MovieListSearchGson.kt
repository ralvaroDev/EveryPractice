package com.example.everypractice.prinoptions.movies.data.models

import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.google.gson.annotations.SerializedName
import java.util.Collections


data class MovieSearchGson(

    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<MovieSearchElementGson> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null

)

data class MovieSearchElementGson(

    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("popularity") var popularity: Double? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null

){
    val posterUrl: String
        get() = BASE_POSTER_IMAGE_URL.plus(posterPath)

}

/*

data class MovieSearch(
    @Json(name = "page") var page: Int? = null,
    @Json(name = "results") var results: ArrayList<MovieSearchElement> = arrayListOf(),
    @Json(name = "total_pages") var totalPages: Int? = null,
    @Json(name = "total_results") var totalResults: Int? = null
)

data class MovieSearchElement(

    @Json(name="adult") var adult: Boolean? = null,
    @Json(name="backdrop_path") var backdropPath: String? = null,
    @Json(name="genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @Json(name="id") var id: Int? = null,
    @Json(name="original_language") var originalLanguage: String? = null,
    @Json(name="original_title") var originalTitle: String? = null,
    @Json(name="overview") var overview: String? = null,
    @Json(name="popularity") var popularity: Double? = null,
    @Json(name="poster_path") var posterPath: String? = null,
    @Json(name="release_date") var releaseDate: String? = null,
    @Json(name="title") var title: String? = null,
    @Json(name="video") var video: Boolean? = null,
    @Json(name="vote_average") var voteAverage: Double? = null,
    @Json(name="vote_count") var voteCount: Int? = null
)
*/
