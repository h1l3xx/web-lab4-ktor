package com.example.security.jwt

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val SECRET_KEY = "2468571396584126483682751223365669522586339325"

private const val ALG = "HmacSHA1"

private val HASH_KEY = hex(SECRET_KEY)

private val HMAC_KEY = SecretKeySpec(HASH_KEY, ALG)

fun hash(password : String) : String{
    val hmac = Mac.getInstance(ALG)
    hmac.init(HMAC_KEY)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
