package com.example.dto

import io.ktor.server.auth.*

data class UserPrincipalDto(
    val id : Int
) : Principal