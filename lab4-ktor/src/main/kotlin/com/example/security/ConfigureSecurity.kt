package com.example.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(){
    JwtConfig.init("web-lab4-ktor")
    install(Authentication){
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val claim = it.payload.getClaim(JwtConfig.CLAIM).asInt()
                if (claim != null){
                    UserPrincipal(claim)
                }else{
                    null
                }
            }
        }
    }
}