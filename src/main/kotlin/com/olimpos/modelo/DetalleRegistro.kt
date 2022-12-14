package com.olimpos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class DetalleRegistro(val idP:Int,
                           val tipo:String,
                           val pregunta:String,
                           val idHumano:String,
                           val respuesta:String)
