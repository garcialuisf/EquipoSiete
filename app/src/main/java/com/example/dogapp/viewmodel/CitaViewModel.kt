// ViewModel que gestiona el estado y la lógica de presentación de citas para la interfaz de usuario.
package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.AppDatabase
import com.example.dogapp.model.Cita
import com.example.dogapp.repository.CitaRepository
import kotlinx.coroutines.launch

class CitaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CitaRepository
    val listaCitas: LiveData<List<Cita>>

    private val _citaSeleccionada = MutableLiveData<Cita?>()
    val citaSeleccionada: LiveData<Cita?> get() = _citaSeleccionada

    init {
        val citaDao = AppDatabase.getInstance(application).citaDao()
        repository = CitaRepository(citaDao)
        listaCitas = repository.listaCitas
    }

    fun guardarCita(cita: Cita) {
        viewModelScope.launch {
            repository.guardarCita(cita)
        }
    }

    fun actualizarCita(cita: Cita) {
        viewModelScope.launch {
            repository.actualizarCita(cita)
        }
    }

    fun eliminarCita(cita: Cita) {
        viewModelScope.launch {
            repository.eliminarCita(cita)
        }
    }

    fun cargarCita(id: Int) {
        viewModelScope.launch {
            _citaSeleccionada.value = repository.obtenerCitaPorId(id)
        }
    }
}
