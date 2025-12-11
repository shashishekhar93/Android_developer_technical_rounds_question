package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("inspection")
    val inspection: Inspection,
    @SerializedName("make")
    val make: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("registrationDate")
    val registrationDate: String,
    @SerializedName("registrationNumber")
    val registrationNumber: String,
    @SerializedName("type")
    val type: String
)