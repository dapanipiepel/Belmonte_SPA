
package com.example.proyectobelmonte1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proyectobelmonte1.model.Viaje

@Database(
    entities = [Viaje::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(EstadoViajeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun viajeDao(): ViajeDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "belmonte_database"
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
