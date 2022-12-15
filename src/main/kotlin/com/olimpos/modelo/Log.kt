package com.olimpos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Log(
    val nombre:String,
    val email:String,
    val password:String,
    val rol:Int
)
