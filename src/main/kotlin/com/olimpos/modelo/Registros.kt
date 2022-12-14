package com.olimpos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Registros(val idP:Int,
                     val idHumano:String,
                     val respuesta:String
)
