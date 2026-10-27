
package com.example.proyectobelmonte1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesion")
data class Sesion(
    @PrimaryKey
    val id: Int = 1,

    val correo: String,

    val rol: String,

    val token: String? = null
)
