package com.example.dogapp.repository

import com.example.dogapp.model.BreedImageResponse
import com.example.dogapp.webservice.ApiService
import com.example.dogapp.webservice.ApiUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RazaRepository(private val apiService: ApiService = ApiUtils.apiService) {

    suspend fun obtenerRazas(): List<String> = withContext(Dispatchers.IO) {
        consultar {
            val respuesta = apiService.obtenerRazas()
            if (respuesta.status == "success") respuesta.message.keys.sorted() else emptyList()
        }.orEmpty()
    }

    suspend fun obtenerImagenPorRaza(raza: String): String = withContext(Dispatchers.IO) {
        val nombreRaza = raza.trim().lowercase()
        val imagen = nombreRaza.takeIf { it.isNotEmpty() }?.let {
            consultarImagen { apiService.obtenerImagenPorRaza(it) }
        }

        imagen ?: consultarImagen { apiService.obtenerImagenAleatoria() }.orEmpty()
    }

    private suspend fun consultarImagen(llamada: suspend () -> BreedImageResponse): String? =
        consultar {
            val respuesta = llamada()
            if (respuesta.status == "success") respuesta.message.takeIf { it.isNotBlank() }
            else null
        }

    private suspend fun <T> consultar(llamada: suspend () -> T): T? = try {
        llamada()
    } catch (cancelacion: CancellationException) {
        throw cancelacion
    } catch (_: Exception) {
        null
    }
}
