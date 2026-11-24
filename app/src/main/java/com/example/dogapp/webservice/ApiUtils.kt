package com.example.dogapp.webservice

object ApiUtils {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}
