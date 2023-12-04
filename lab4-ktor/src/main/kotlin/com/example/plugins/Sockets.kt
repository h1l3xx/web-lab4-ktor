package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration

fun Application.configureSockets() {
    val sessions = mutableMapOf<Int, DefaultWebSocketServerSession>()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") {
            for (frame in incoming){
                var message = ""
                if (frame is Frame.Text) {
                    message = frame.readText()
                }
                println("message : $message")
                val words = message.split(" ")
                val id = words[0].toInt()
                if (!sessions.containsKey(id)){
                    sessions[id] = this
                    println(sessions)
                    outgoing.send(Frame.Text("connected"))
                }else{
                    if (sessions.containsKey(id) && words[1] == "connect"){
                        val oldUser = sessions[id]!!
                        oldUser.close()
                        sessions[id] = this
                        println(sessions)
                        outgoing.send(Frame.Text("connected"))
                    }else{
                        outgoing.send(Frame.Text("yes"))
                    }
                }
            }
        }
    }
}
