package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Pan(
    @SerializedName("image")
    val image: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("verified")
    val verified: Boolean
)