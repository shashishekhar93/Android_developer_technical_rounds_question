package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Aadhaar(
    @SerializedName("imageBack")
    val imageBack: String,
    @SerializedName("imageFront")
    val imageFront: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("verified")
    val verified: Boolean
)