package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResultDto (
    val success : Boolean,
    val error: ErrorDto?,
    val token : String?,
    val id : Int?
) {
}