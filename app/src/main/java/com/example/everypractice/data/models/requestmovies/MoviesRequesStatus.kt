package com.example.everypractice.data.models.requestmovies

import com.example.everypractice.data.models.*

enum class DataResponseStatus{
    DONE, LOADING, ERROR
}

data class LoadMoviesResult(
    val status: DataResponseStatus,
    val data: List<PermanentFavouriteMovies>
)