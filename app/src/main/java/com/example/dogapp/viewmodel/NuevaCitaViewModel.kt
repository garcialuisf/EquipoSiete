package com.example.dogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
}
