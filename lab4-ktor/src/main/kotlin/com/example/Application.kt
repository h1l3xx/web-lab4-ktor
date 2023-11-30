package com.example

import com.example.plugins.*
import com.example.security.configureSecurity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

fun main() {

    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}


fun Application.module() {

    val database = configureConnect()

    val userService = UserService(database)
    val resultsService = ResultsService(database)

    configureSecurity()
    configureSerialization()
    configureSockets()
    configureDatabases(userService, resultsService)
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
}
