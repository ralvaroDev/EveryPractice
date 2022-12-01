package com.example.everypractice.data.models

//TODO ESTE DATA CLASS DEBE IR AQUI O ENCIMA DE SU USE CASE?
data class LoginCredentials(
    val email: String,
    val password: String,
    val nameUser: String?
)
