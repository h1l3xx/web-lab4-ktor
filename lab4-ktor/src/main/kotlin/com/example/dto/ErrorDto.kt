package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val code : Int,
    val name : String,
    val description : String?
)