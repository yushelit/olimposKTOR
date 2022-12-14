package com.olimpos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Pruebas(val id:Int,
                   val tipo:String,
                   val pregunta:String,
                   val atributo:String,
                   val destino:Int,
                   val respCorrecta:String
            )
