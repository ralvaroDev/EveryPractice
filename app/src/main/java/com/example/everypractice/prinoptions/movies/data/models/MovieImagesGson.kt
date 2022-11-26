package com.example.everypractice.prinoptions.movies.data.models

import com.google.gson.annotations.SerializedName

data class MovieImagesGson(

    var backdrops: ArrayList<Backdrops> = arrayListOf(),
    var id: Int? = null,
    var logos: ArrayList<Logos> = arrayListOf(),
    var posters: ArrayList<Posters> = arrayListOf()

)

data class Backdrops(

    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int? = null

)

data class Logos(

    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int? = null

)

data class Posters(

    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int? = null

)