package com.olimpos.plugins

import com.olimpos.rutas.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respond(mapOf("hello" to "world"))
        }
        humanRouting()
        userRouting()
        godRouting()
        regRouting()
        trialsRouting()
    }
}
