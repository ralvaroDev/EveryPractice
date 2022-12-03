package com.example.everypractice.data.models.login

data class UserStatus(
    val status: UserResponseStatus,
    val value: Any? = null
)

enum class UserResponseStatus {
    DONE, ERROR,
    //LOADING
}