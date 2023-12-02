package com.example

import com.example.plugins.*
import com.example.security.configureSecurity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object Const{
    const val DELAY : Long = 60000
}
fun main() {

    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
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

    install(CORS) {
        // The methods for your server routes
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        anyHost()
    }
    launch {
        while (true){
            delay(Const.DELAY)
            cacheService.refresh()
        }
    }
}
