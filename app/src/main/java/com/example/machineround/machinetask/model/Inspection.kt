package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Inspection(
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("status")
    val status: String
)