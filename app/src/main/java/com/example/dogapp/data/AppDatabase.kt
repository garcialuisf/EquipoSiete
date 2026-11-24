// Base de datos Room de la aplicación que gestiona el acceso y la instancia singleton.
package com.example.dogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogapp.model.Cita
import com.example.dogapp.utils.Constants

@Database(entities = [Cita::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DB_NAME,
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
