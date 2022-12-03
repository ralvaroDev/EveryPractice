package com.example.everypractice.data.models.login

//TODO ESTE DATA CLASS DEBE IR AQUI O ENCIMA DE SU USE CASE?
data class LoginCredentials(
    val email: String,
    val password: String,
    val nameUser: String?
)
