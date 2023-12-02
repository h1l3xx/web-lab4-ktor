package com.example.security

import io.ktor.server.auth.*

data class UserPrincipal(
    val id : Int
) : Principal