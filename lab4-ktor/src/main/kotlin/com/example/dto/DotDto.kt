package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class DotDto(
    val x : Double,
    val y : Double,
    val r : Double,
    val id : Int
)
