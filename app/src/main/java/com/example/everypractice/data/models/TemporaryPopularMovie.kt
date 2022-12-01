package com.example.everypractice.data.models

data class TemporaryPopularMovie(
    var page: Int? = null,
    var results: List<TemporaryPopularMovieElement> = listOf(),
    var totalPages: Int? = null,
    var totalResults: Int? = null
)

data class TemporaryPopularMovieElement(
    var backdropPathUrl: String,
    var genreIds: List<Int>,
    var id: Int,
    var overview: String,
    var posterPathUrl: String,
    var title: String,
    var video: Boolean,
    val voteAverage: Double
)
