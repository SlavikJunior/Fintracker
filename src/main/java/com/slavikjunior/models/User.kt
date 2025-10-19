package com.slavikjunior.models

data class User(
    val id: Int,
    val login: String,
    val hashPassword: String,
    val email: String
)
