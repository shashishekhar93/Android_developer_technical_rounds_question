package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Addresse(
    @SerializedName("city")
    val city: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("line1")
    val line1: String,
    @SerializedName("line2")
    val line2: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("proofImage")
    val proofImage: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("type")
    val type: String
)