package com.example.dogapp.webservice

import com.example.dogapp.model.BreedImageResponse
import com.example.dogapp.model.BreedListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("breeds/list/all")
    suspend fun obtenerRazas(): BreedListResponse

    @GET("breed/{breed}/images/random")
    suspend fun obtenerImagenPorRaza(@Path("breed") raza: String): BreedImageResponse

    @GET("breeds/image/random")
    suspend fun obtenerImagenAleatoria(): BreedImageResponse
}
