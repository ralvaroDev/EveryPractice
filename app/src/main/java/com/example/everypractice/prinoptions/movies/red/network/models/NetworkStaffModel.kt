package com.example.everypractice.prinoptions.movies.red.network.models

import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.example.everypractice.prinoptions.movies.data.domain.TemporaryStaffMovie
import com.google.gson.annotations.SerializedName

data class NetworkStaffFromMovieContainer(
    var id: Int,
    var cast: ArrayList<NetworkCastContainer> = arrayListOf(),
    var crew: ArrayList<NetworkCrewContainer> = arrayListOf()
)

data class NetworkCastContainer(
    var adult: Boolean? = null,
    var gender: Int? = null,
    var id: Int = 0,
    @SerializedName("known_for_department") var knownForDepartment: String = "No department",
    var name: String,
    @SerializedName("original_name") var originalName: String? = null,
    var popularity: Double = 0.0,
    @SerializedName("profile_path") var profilePath: String = "",
    @SerializedName("cast_id") var castId: Int? = null,
    var character: String = "No char",
    @SerializedName("credit_id") var creditId: String? = null,
    var order: Int? = null
){
    val profilePathUrl: String
        get() = BASE_POSTER_IMAGE_URL.plus(profilePath)
}

data class NetworkCrewContainer(
    var adult: Boolean? = null,
    var gender: Int? = null,
    var id: Int? = null,
    @SerializedName("known_for_department") var knownForDepartment: String? = null,
    var name: String? = null,
    @SerializedName("original_name") var originalName: String? = null,
    var popularity: Double? = null,
    @SerializedName("profile_path") var profilePath: String? = null,
    @SerializedName("credit_id") var creditId: String? = null,
    var department: String? = null,
    var job: String? = null
)

fun List<NetworkCastContainer>.asTemporaryDomainModel(): List<TemporaryStaffMovie>{
    return map {
        TemporaryStaffMovie(
            id = it.id,
            knownForDepartment = it.knownForDepartment,
            name = it.name,
            originalName = it.originalName ?: "No Name",
            popularity = it.popularity,
            profilePath = it.profilePathUrl,
            character = it.character
        )
    }
}

