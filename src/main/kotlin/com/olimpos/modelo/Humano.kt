package com.olimpos.modelo

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class Humano (
    val email:String,
    val destino:Int,
    val dios:String,
    val alive:Boolean,
    val dia:Int,
    val mes:Int,
    val year:Int
)
