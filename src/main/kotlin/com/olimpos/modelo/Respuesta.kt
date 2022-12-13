package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Respuesta(val message:String, val status:Int)