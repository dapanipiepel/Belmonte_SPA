
package com.example.proyectobelmonte1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectobelmonte1.model.Sesion
import com.example.proyectobelmonte1.model.Viaje

@Database(
    entities = [Viaje::class],
    entities = [Viaje::class, Sesion::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(EstadoViajeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun viajeDao(): ViajeDao

    abstract fun sesionDao(): SesionDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE viajes ADD COLUMN cantidadPasajeros INTEGER NOT NULL DEFAULT 1"
                )
                db.execSQL(
                    "ALTER TABLE viajes ADD COLUMN vehiculoId INTEGER"
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `sesion` (
                        `id` INTEGER NOT NULL,
                        `correo` TEXT NOT NULL,
                        `rol` TEXT NOT NULL,
                        `token` TEXT,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "belmonte_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
