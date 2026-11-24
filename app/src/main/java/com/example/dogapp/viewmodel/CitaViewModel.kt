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
import com.example.dogapp.repository.RazaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CitaViewModel(application: Application) : AndroidViewModel(application) {

    enum class EstadoEdicion { INICIAL, CARGANDO, LISTA, NO_DISPONIBLE, GUARDANDO, GUARDADA, ERROR }

    enum class EstadoEliminacion { INICIAL, EN_CURSO, ELIMINADA, NO_DISPONIBLE, ERROR }

    private val _estadoEliminacion = MutableLiveData(EstadoEliminacion.INICIAL)
    val estadoEliminacion: LiveData<EstadoEliminacion> get() = _estadoEliminacion

    private val repository: CitaRepository
    private val razaRepository by lazy { RazaRepository() }
    private val _estadoEdicion = MutableLiveData(EstadoEdicion.INICIAL)
    val estadoEdicion: LiveData<EstadoEdicion> get() = _estadoEdicion
    private var citaIdEdicion: Int? = null
    val listaCitas: LiveData<List<Cita>>

    private var cargaCita: Job? = null
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

    fun eliminarCitaSeleccionada(id: Int) {
        if (_estadoEliminacion.value != EstadoEliminacion.INICIAL) return
        val cita = _citaSeleccionada.value?.takeIf { it.id == id } ?: return

        cargaCita?.cancel()
        _estadoEliminacion.value = EstadoEliminacion.EN_CURSO
        viewModelScope.launch {
            _estadoEliminacion.value = try {
                val filasEliminadas = repository.eliminarCita(cita)
                _citaSeleccionada.value = null
                if (filasEliminadas == 1) EstadoEliminacion.ELIMINADA else EstadoEliminacion.NO_DISPONIBLE
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                EstadoEliminacion.ERROR
            }
        }
    }

    fun consumirResultadoEliminacion() {
        _estadoEliminacion.value = EstadoEliminacion.INICIAL
    }

    fun cargarCita(id: Int) {
        if (_estadoEliminacion.value != EstadoEliminacion.INICIAL) return

        cargaCita?.cancel()
        cargaCita = viewModelScope.launch {
            _citaSeleccionada.value = try {
                repository.obtenerCitaPorId(id)
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                null
            }
        }
    }

    fun cargarCitaParaEditar(id: Int) {
        if (citaIdEdicion == id) return
        citaIdEdicion = id
        _estadoEdicion.value = EstadoEdicion.CARGANDO
        viewModelScope.launch {
            try {
                val cita = repository.obtenerCitaPorId(id)
                _citaSeleccionada.value = cita
                _estadoEdicion.value = if (cita == null) EstadoEdicion.NO_DISPONIBLE else EstadoEdicion.LISTA
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _estadoEdicion.value = EstadoEdicion.NO_DISPONIBLE
            }
        }
    }

    fun puedeEditarCita(nombre: String, raza: String, propietario: String, telefono: String): Boolean {
        return nombre.isNotBlank() && nombre.length <= 15 &&
            raza.isNotBlank() &&
            propietario.isNotBlank() && propietario.length <= 30 &&
            telefono.isNotEmpty() && telefono.length <= 10 && telefono.all { it in '0'..'9' }
    }

    fun editarCita(nombre: String, raza: String, propietario: String, telefono: String) {
        val original = _citaSeleccionada.value ?: return
        val estado = _estadoEdicion.value
        if (estado != EstadoEdicion.LISTA && estado != EstadoEdicion.ERROR) return
        if (!puedeEditarCita(nombre, raza, propietario, telefono)) return

        _estadoEdicion.value = EstadoEdicion.GUARDANDO
        viewModelScope.launch {
            try {
                val razaNueva = raza.trim()
                val imagen = if (razaNueva.equals(original.raza.trim(), ignoreCase = true)) {
                    original.imagenUrl
                } else {
                    razaRepository.obtenerImagenPorRaza(razaNueva)
                }
                val actualizada = original.copy(
                    nombreMascota = nombre.trim(),
                    raza = razaNueva,
                    propietario = propietario.trim(),
                    telefono = telefono,
                    imagenUrl = imagen,
                )
                _estadoEdicion.value = if (repository.actualizarCita(actualizada) == 1) {
                    EstadoEdicion.GUARDADA
                } else {
                    EstadoEdicion.NO_DISPONIBLE
                }
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (_: Exception) {
                _estadoEdicion.value = EstadoEdicion.ERROR
            }
        }
    }
}
