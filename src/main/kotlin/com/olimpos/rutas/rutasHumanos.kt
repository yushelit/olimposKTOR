package com.olimpos.rutas

import ConexionEstatica
import com.olimpos.modelo.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import modelo.Respuesta

fun Route.humanRouting(){
    route("/humanos"){
        get{
            val humanos = ConexionEstatica.obtenerHumanos()
            if (humanos.isNotEmpty()){
                call.respond(humanos)
            }
            else {
                call.respondText("No hay humanos",status = HttpStatusCode.OK)
            }
        }
        get("{email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val humano = ConexionEstatica.obtenerHumano(id)
            if (humano == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("El humano con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
            }
            call.respond(humano)
        }

        post{
            val us = call.receive<Humano>()
            ConexionEstatica.agregarHumano(us)
            call.respondText("Humano nacido",status = HttpStatusCode.Created)
        }
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Humano>()
            ConexionEstatica.modificarHumano(id, us)
        }
    }
    route("/afinidad"){
        get("{afinidad?}"){
            val afinidad = call.parameters["afinidad"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val humano = ConexionEstatica.obtenerAfines(afinidad)
            call.respond(humano)
        }
    }

}
fun Route.userRouting(){
    route("/usuarios"){
        route("{email?}"){
            get("{password?}"){
                val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
                val pwd = call.parameters["password"] ?: return@get call.respondText("password vacía en la url", status = HttpStatusCode.BadRequest)
                val log: Log? = ConexionEstatica.login(id, pwd)
                if (log == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@get call.respond(Respuesta("El usuario con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
                }
                call.respond(log)
            }
        }
    }

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
        get("{email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val usuario = ConexionEstatica.obtenerUsuario(id)
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
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Usuario>()
            ConexionEstatica.modificarUsuario(id, us)
        }
    }
}
fun Route.godRouting(){
    route("/dioses"){
        get{
            val dioses = ConexionEstatica.obtenerDioses()
            if (dioses.isNotEmpty()){
                call.respond(dioses)
            }
            else {
                call.respondText("No hay registros",status = HttpStatusCode.OK)
            }
        }
        get("{email?}"){
            val id = call.parameters["email"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val usuario = ConexionEstatica.obtenerDios(id)
            if (usuario == null) {
                call.response.status(HttpStatusCode.NotFound)
                return@get call.respond(Respuesta("El usuario con el loggin: ${id} no existe", HttpStatusCode.NotFound.value))
            }
            call.respond(usuario)
        }
        put("{email?}") {
            val id = call.parameters["email"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Usuario>()
            ConexionEstatica.modificarDios(id, us)
        }
    }
}
fun Route.regRouting(){
    route("/registros"){
        route("{id?}"){
            get("{mail?}"){
                val id = call.parameters["id"] ?: return@get call.respondText("id vacia en la url", status = HttpStatusCode.BadRequest)
                val email = call.parameters["mail"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
                val detalle: DetalleRegistro? = ConexionEstatica.obtenerRegistro(email, Integer.parseInt(id))
                if (detalle == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@get call.respond(Respuesta("La prueba $id no esta asociada al usuario $email", HttpStatusCode.NotFound.value))
                }
                call.respond(detalle)
            }
            put("{mail?}") {
                val id = call.parameters["id"] ?: return@put call.respondText("id vacia en la url", status = HttpStatusCode.BadRequest)
                val mail = call.parameters["mail"] ?: return@put call.respondText("id vacío en la url", status = HttpStatusCode.BadRequest)
                val reg = call.receive<Registros>()
                ConexionEstatica.modificarRegistro(mail, Integer.parseInt(id), reg)
            }
        }
        get("{mail?}"){
            val email = call.parameters["mail"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val detalle = ConexionEstatica.obtenerRegistros(email)
            if (detalle.isNotEmpty()){
                call.respond(detalle)
            }
            else {
                call.respondText("No hay usuarios",status = HttpStatusCode.OK)
            }
        }
        post{
            val reg = call.receive<Registros>()
            ConexionEstatica.insertarRegistro(reg)
            call.respondText("Registro creado",status = HttpStatusCode.Created)
        }
    }


}
fun Route.trialsRouting() {
    route("/pruebas"){
        get {
           val tipos = ConexionEstatica.obtenerTipos()
           if(tipos.isNotEmpty()){
              call.respond(tipos)
           }else {
               call.respondText("No hay pruebas registradas",status = HttpStatusCode.OK)
           }
        }
        get("{tipo?}"){
            val tipo = call.parameters["tipo"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val pruebas = ConexionEstatica.obtenerPruebasPorTipo(tipo)
            if(pruebas.isNotEmpty()){
                call.respond(pruebas)
            }else {
                call.respondText("No hay pruebas registradas de tipo $tipo",status = HttpStatusCode.OK)
            }
        }
        get("{id?}"){
            val id = call.parameters["id"] ?: return@get call.respondText("email vacío en la url", status = HttpStatusCode.BadRequest)
            val prueba = ConexionEstatica.obtenerPrueba(Integer.parseInt(id))
            if(prueba != null){
                call.respond(prueba)
            }
            else {
                call.respondText("prueba no encontrada",status = HttpStatusCode.OK)
            }
        }
    }
}