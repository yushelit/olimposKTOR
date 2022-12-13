package com.olimpos.plugins

import com.olimpos.rutas.godRouting
import com.olimpos.rutas.humanRouting
import com.olimpos.rutas.userRouting
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
    }
}
