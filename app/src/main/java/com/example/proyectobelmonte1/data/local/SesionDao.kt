
package com.example.proyectobelmonte1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectobelmonte1.model.Sesion

@Dao
interface SesionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSesion(sesion: Sesion)

    @Query("SELECT * FROM sesion WHERE id = 1 LIMIT 1")
    suspend fun obtenerSesion(): Sesion?

    @Query("DELETE FROM sesion")
    suspend fun cerrarSesion()
}
