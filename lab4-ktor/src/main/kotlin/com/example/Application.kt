package com.example

import com.example.database.*
import com.example.plugins.*
import com.example.security.auth.authRoutes
import com.example.security.configureSecurity
import com.example.security.cors.configureCORS
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object Const{
    const val DELAY : Long = 60000
}
fun main() {

    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    val database = configureConnect()

    val userService = UserService(database)
    val resultsService = ResultsService(database)
    val cacheService = CacheService(database)

    configureSecurity()
    configureSerialization()
    configureSockets()
    configureDatabases(userService, resultsService, cacheService)
    authRoutes(userService)
    configureCORS()
    launch {
        while (true){
            delay(Const.DELAY)
            cacheService.refresh()
        }
    }
}
