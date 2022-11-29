package com.example.everypractice.data.domain

data class TemporaryStaffMovie(
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String,
    val character: String,
)