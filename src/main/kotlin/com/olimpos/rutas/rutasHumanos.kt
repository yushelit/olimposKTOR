package com.olimpos.rutas

import com.olimpos.modelo.Humano
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import modelo.Respuesta

private val humanos = arrayListOf<Humano>()
private val tartaro = arrayListOf<Humano>()
private val elisium = arrayListOf<Humano>()

fun Route.humanRouting(){
    route("/humano"){
        get{
            if (humanos.isNotEmpty()){
                call.respond(humanos)
            }
            else {
                call.respondText("No hay usuarios",status = HttpStatusCode.OK)
            }
        }
        get("{email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)

            try {
                //val pruebaParam = id
            }catch(e:Exception){
                call.response.status(HttpStatusCode.BadRequest)
                return@get call.respond(Respuesta("El email ${id} no es válido", HttpStatusCode.BadRequest.value))
            }
            val humano = humanos.find { it.email == id }
            if (humano == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("El humano con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
            }
            call.respond(humano)
        }
        post{
            val us = call.receive<Humano>()
            humanos.add(us)
            call.respondText("Humano nacido",status = HttpStatusCode.Created)
        }
        delete("{email?}") {
            val id = call.parameters["email"] ?: return@delete call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val muerto = humanos.find { it.email == id }
            if (humanos.removeIf { it.email == id }){
                call.respondText("Humano muerto",status = HttpStatusCode.Accepted)
                if(muerto?.destino!! > 100){
                    elisium.add(muerto)
                }else{
                    tartaro.add(muerto)
                }
            }
            else {
                call.respondText("No encontrado",status = HttpStatusCode.NotFound)
            }
        }
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Humano>()
            val pos = humanos.indexOfFirst{ it.email == id}
            if (pos == -1){
                call.respondText("No encontrado",status = HttpStatusCode.NotFound)
            }
            else {
                humanos[pos] = us
                call.respondText("humano modificado",status = HttpStatusCode.Accepted)
            }
        }

    }
    route("/tartaro"){
        get{
            if (tartaro.isNotEmpty()){
                call.respond(tartaro)
            }
            else {
                call.respondText("No hay usuarios",status = HttpStatusCode.OK)
            }
        }
    }
    route("/campos"){
        get{
            if (elisium.isNotEmpty()){
                call.respond(elisium)
            }
            else {
                call.respondText("No hay usuarios",status = HttpStatusCode.OK)
            }
        }
    }
}