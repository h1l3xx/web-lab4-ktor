package com.example.security.auth

import com.example.database.UserService
import com.example.dto.AuthDto
import com.example.dto.UserDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(userService: UserService){
    routing {
        route("/auth"){
            post("/register"){
                val params = call.receive<UserDto>()
                val result = userService.create(params)
                call.respond(HttpStatusCode.Created, result)
            }
            post{
                val params = call.receive<AuthDto>()
                val result = userService.auth(params)
                call.respond(result)
            }
        }
    }
}