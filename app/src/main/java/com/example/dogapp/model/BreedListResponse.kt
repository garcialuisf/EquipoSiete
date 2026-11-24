package com.example.dogapp.model

import com.google.gson.annotations.SerializedName

data class BreedListResponse(
    @SerializedName("message") val message: Map<String, List<String>>,
    @SerializedName("status") val status: String,
)
