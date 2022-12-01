package com.example.everypractice.data.models

data class TemporaryImageMovie(
    var backdrops: List<TemporaryBackdropsElement> = arrayListOf(),
    var id: Int,
    var posters: List<TemporaryPosterElement> = arrayListOf(),
)

data class TemporaryBackdropsElement(
    val aspectRatio: Double,
    val height: Int,
    val fileBackdropPathUrl: String,
    val width: Int
)

data class TemporaryPosterElement(
    val aspectRatio: Double,
    val height: Int,
    val filePosterPathUrl: String,
    val width: Int
)

data class AllImages(
    val aspectRatio: Double,
    val height: Int,
    val filePosterPathUrl: String,
    val width: Int
)

fun List<TemporaryBackdropsElement>.toUnitedObject1(): List<AllImages> {
    return map {
        AllImages(
            aspectRatio = it.aspectRatio,
            height = it.height,
            filePosterPathUrl = it.fileBackdropPathUrl,
            width = it.width
        )
    }
}

fun List<TemporaryPosterElement>.toUnitedObject2(): List<AllImages> {
    return map {
        AllImages(
            aspectRatio = it.aspectRatio,
            height = it.height,
            filePosterPathUrl = it.filePosterPathUrl,
            width = it.width
        )
    }
}