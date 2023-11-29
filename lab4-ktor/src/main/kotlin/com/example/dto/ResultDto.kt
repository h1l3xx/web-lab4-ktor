package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val x : Double,
    val y : Double,
    val r : Double,
    val ownerId : Int,
    val result: Boolean,
    val execTimeInMicroSec: Int?,
    val error : ErrorDto?
) {
}