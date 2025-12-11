package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("capturedAt")
    val capturedAt: String,
    @SerializedName("fileUrl")
    val fileUrl: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("title")
    val title: String
)