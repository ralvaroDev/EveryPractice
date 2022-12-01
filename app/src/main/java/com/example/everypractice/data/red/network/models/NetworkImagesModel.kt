package com.example.everypractice.data.red.network.models

import com.example.everypractice.consval.BASE_BACKGROUND_IMAGE_URL
import com.example.everypractice.data.models.TemporaryBackdropsElement
import com.example.everypractice.data.models.TemporaryImageMovie
import com.example.everypractice.data.models.TemporaryPosterElement
import com.google.gson.annotations.SerializedName

data class NetworkImagesMovieContainer(
    var backdrops: ArrayList<NetworkBackdropsContainer> = arrayListOf(),
    var id: Int = 0,
    var logos: ArrayList<NetworkLogosContainer> = arrayListOf(),
    var posters: ArrayList<NetworkPostersContainer> = arrayListOf(),
)

data class NetworkBackdropsContainer(
    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int? = null,
) {
    val fileBackdropPathUrl: String
        get() = BASE_BACKGROUND_IMAGE_URL.plus(filePath)
}

data class NetworkLogosContainer(
    @SerializedName("aspect_ratio") var aspectRatio: Double = 0.0,
    var height: Int = 0,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int = 0,
)

data class NetworkPostersContainer(
    @SerializedName("aspect_ratio") var aspectRatio: Double? = null,
    var height: Int? = null,
    @SerializedName("iso_639_1") var iso6391: String? = null,
    @SerializedName("file_path") var filePath: String? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    var width: Int? = null,
) {
    val filePosterPathUrl: String
        get() = BASE_BACKGROUND_IMAGE_URL.plus(filePath)
}

fun NetworkImagesMovieContainer.asTemporaryDomainModel(): TemporaryImageMovie {
    return TemporaryImageMovie(
        backdrops = backdrops.asInsideTemporaryBackdropDomainModel(),
        id = id,
        posters = posters.asInsideTemporaryPosterDomainModel()
    )
}

fun List<NetworkBackdropsContainer>.asInsideTemporaryBackdropDomainModel(): List<TemporaryBackdropsElement> {
    return map {
        TemporaryBackdropsElement(
            aspectRatio = it.aspectRatio ?: 0.0,
            height = it.height ?: 0,
            fileBackdropPathUrl = it.fileBackdropPathUrl,
            width = it.width ?: 0
        )
    }
}

fun List<NetworkPostersContainer>.asInsideTemporaryPosterDomainModel(): List<TemporaryPosterElement> {
    return map {
        TemporaryPosterElement(
            aspectRatio = it.aspectRatio ?: 0.0,
            height = it.height ?: 0,
            filePosterPathUrl = it.filePosterPathUrl,
            width = it.width ?: 0
        )
    }
}



