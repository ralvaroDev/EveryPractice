package com.example.everypractice.data.models

data class TemporarySearchMovie(
    var page: Int? = null,
    var results: List<TemporarySearchMovieElement> = listOf(),
    var totalPages: Int? = null,
    var totalResults: Int? = null
)

data class TemporarySearchMovieElement(
    var adult: Boolean,
    var backdropPathUrl: String,
    var genreIds: List<Int>,
    var id: Int,
    var overview: String,
    var popularity: Double,
    var posterPathUrl: String,
    var releaseDate: String? = "",
    var title: String,
    var video: Boolean,
    val voteAverage: Double
)


