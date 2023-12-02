package com.example.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

class JwtConfig private constructor(secret : String){
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier : JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun createAccessToken(id : Int) : String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(CLAIM, id)
        .sign(algorithm)
    companion object{
        private const val ISSUER = "web-lab4-ktor"
        private const val AUDIENCE = "web-lab4-ktor"
        const val CLAIM = "id"

        lateinit var instance : JwtConfig

        fun init(secret : String){
            synchronized(this){
                if (!this::instance.isInitialized){
                    instance = JwtConfig(secret)
                }
            }
        }

    }
}