package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Emi(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("schedule")
    val schedule: String,
    @SerializedName("status")
    val status: String
)