package com.example.plugins

import com.example.dto.DotDto
import com.example.dto.ResultDto
import com.example.dto.UserDto
import com.example.utils.Checker
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases(userService: UserService, resultsService: ResultsService) {

    val checker = Checker()

    routing {
        // Create user
        post("/users") {
            val user = call.receive<UserDto>()
            userService.create(user)
            call.respond(HttpStatusCode.Created)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<UserDto>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
        authenticate {
            route("/results"){
                post("/check"){
                    val dot = call.receive<DotDto>()
                    val result = checker.post(dot)
                    resultsService.create(result)
                    call.respond(HttpStatusCode.Created, result)
                }
                get("/user/{id}"){
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                    val results = resultsService.getByUserId(id)
                    call.respond(HttpStatusCode.OK, results)
                }
                delete("/user/{id}"){
                    val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                    resultsService.deleteByUserId(id)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
