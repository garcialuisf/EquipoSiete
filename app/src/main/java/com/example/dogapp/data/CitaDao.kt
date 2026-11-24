// Objeto de acceso a datos (DAO) que define las operaciones de base de datos para la entidad Cita.
package com.example.dogapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dogapp.model.Cita

@Dao
interface CitaDao {

    @Insert
    suspend fun insertar(cita: Cita)

    @Update
    suspend fun actualizar(cita: Cita): Int

    @Delete
    suspend fun eliminar(cita: Cita)

    @Query("SELECT * FROM citas ORDER BY id ASC")
    fun obtenerTodas(): LiveData<List<Cita>>

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Cita?
}
