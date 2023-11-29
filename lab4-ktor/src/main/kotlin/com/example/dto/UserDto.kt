package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email : String,
    val name : String,
    val birthday : String,
    val login : String,
    val password : String
)
