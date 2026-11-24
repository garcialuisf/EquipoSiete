// Repositorio que centraliza el acceso a los datos de citas ejecutando operaciones en segundo plano.
package com.example.dogapp.repository

import androidx.lifecycle.LiveData
import com.example.dogapp.data.CitaDao
import com.example.dogapp.model.Cita
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitaRepository(private val citaDao: CitaDao) {

    val listaCitas: LiveData<List<Cita>> = citaDao.obtenerTodas()

    suspend fun guardarCita(cita: Cita) = withContext(Dispatchers.IO) {
        citaDao.insertar(cita)
    }

    suspend fun actualizarCita(cita: Cita): Int = withContext(Dispatchers.IO) {
        citaDao.actualizar(cita)
    }

    suspend fun eliminarCita(cita: Cita): Int = withContext(Dispatchers.IO) {
        citaDao.eliminar(cita)
    }

    suspend fun obtenerCitaPorId(id: Int): Cita? = withContext(Dispatchers.IO) {
        citaDao.obtenerPorId(id)
    }
}
