package com.example.dogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogapp.repository.RazaRepository
import kotlinx.coroutines.launch

class RazaViewModel : ViewModel() {
    private val repository = RazaRepository()
    private val _razas = MutableLiveData<List<String>>()
    val razas: LiveData<List<String>> get() = _razas

    init {
        viewModelScope.launch {
            _razas.value = repository.obtenerRazas()
        }
    }

    suspend fun obtenerImagenPorRaza(raza: String): String =
        repository.obtenerImagenPorRaza(raza)
}
