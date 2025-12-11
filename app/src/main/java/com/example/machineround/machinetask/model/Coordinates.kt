package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("updatedAt")
    val updatedAt: String
)