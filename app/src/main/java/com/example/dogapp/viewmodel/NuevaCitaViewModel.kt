package com.example.dogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogapp.model.Cita

class NuevaCitaViewModel : ViewModel() {
    val nombre = MutableLiveData("")
    val raza = MutableLiveData("")
    val propietario = MutableLiveData("")
    val telefono = MutableLiveData("")

    fun puedeGuardar(
        nombre: String?,
        raza: String?,
        propietario: String?,
        telefono: String?
    ): Boolean = !nombre.isNullOrBlank() && !raza.isNullOrBlank() &&
        !propietario.isNullOrBlank() && !telefono.isNullOrBlank()

    fun tieneSintomaSeleccionado(posicion: Int): Boolean = posicion > 0

    fun prepararCita(sintoma: String): Cita? {
        val nombreActual = nombre.value.orEmpty().trim()
        val razaActual = raza.value.orEmpty().trim()
        val propietarioActual = propietario.value.orEmpty().trim()
        val telefonoActual = telefono.value.orEmpty()
        if (!puedeGuardar(nombreActual, razaActual, propietarioActual, telefonoActual)) return null
        if (sintoma.isBlank()) return null

        return Cita(
            nombreMascota = nombreActual,
            raza = razaActual,
            propietario = propietarioActual,
            telefono = telefonoActual,
            sintoma = sintoma,
        )
    }
}
