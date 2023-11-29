package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val login : String,
    val password : String
)
