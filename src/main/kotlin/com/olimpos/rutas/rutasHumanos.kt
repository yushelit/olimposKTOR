package com.olimpos.rutas

import ConexionEstatica
import com.olimpos.modelo.Humano
import com.olimpos.modelo.Usuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import modelo.Respuesta



fun Route.humanRouting(){
    route("/humano"){
        get{
            val humanos = ConexionEstatica.obtenerHumanos()
            if (humanos.isNotEmpty()){
                call.respond(humanos)
            }
            else {
                call.respondText("No hay humanos",status = HttpStatusCode.OK)
            }
        }
        get("{/email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val humano = ConexionEstatica.obtenerHumano(id)
            if (humano == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("El humano con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
            }
            call.respond(humano)
        }
        get("{/afinidad?}"){
            val afinidad = call.parameters["afinidad"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val humano = ConexionEstatica.obtenerAfines(afinidad)
            if (humano == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("No hay humanos afines al dios $afinidad", HttpStatusCode.NotFound.value))
            }
            call.respond(humano)
        }
        post{
            val us = call.receive<Humano>()
            ConexionEstatica.agregarHumano(us)
            call.respondText("Humano nacido",status = HttpStatusCode.Created)
        }
//        delete("{email?}") {
//            val id = call.parameters["email"] ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
//
//
//            else {
//                call.respondText("No encontrado",status = HttpStatusCode.NotFound)
//            }
//        }
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Humano>()
            ConexionEstatica.modificarHumano(id, us)
        }
    }
}
fun Route.userRouting(){
    route("/usuarios"){
        get{
            val usuario = ConexionEstatica.obtenerUsuarios()
            if (usuario.isNotEmpty()){
                call.respond(usuario)
            }
            else {
                call.respondText("No hay usuarios",status = HttpStatusCode.OK)
            }
        }
        get("{/email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val usuario = ConexionEstatica.obtenerHumano(id)
            if (usuario == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("El usuario con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
            }
            call.respond(usuario)
        }
        post{
            val us = call.receive<Usuario>()
            ConexionEstatica.agregarUsuario(us)
            call.respondText("Usuario agregado",status = HttpStatusCode.Created)
        }
//        delete("{email?}") {
//            val id = call.parameters["email"] ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
//
//
//            else {
//                call.respondText("No encontrado",status = HttpStatusCode.NotFound)
//            }
//        }
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Humano>()
            ConexionEstatica.modificarHumano(id, us)
        }
    }
}