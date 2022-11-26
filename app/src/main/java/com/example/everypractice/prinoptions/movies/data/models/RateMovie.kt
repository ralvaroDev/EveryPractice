package com.example.everypractice.prinoptions.movies.data.models

import com.squareup.moshi.Json

data class RateMovieRequest(
    val value : List<Value>
)

data class Value(
    @Json(name = "value") val valueRateMovie : Double
)

data class RateMovieResponse(
    @Json(name = "status_code")val statusCode : Int,
    @Json(name = "status_message")val statusMessage : String
)


