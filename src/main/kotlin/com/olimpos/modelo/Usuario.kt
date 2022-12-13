package com.olimpos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(val nombre:String,
                  val email:String,
                  val password:String,
                  val sabiduria:Int,
                  val nobleza:Int,
                  val virtud:Int,
                  val maldad:Int,
                  val audacia:Int,
                  val rol:Int
    )
