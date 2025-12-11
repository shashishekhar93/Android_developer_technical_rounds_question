package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("apiVersion")
    val apiVersion: String,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("timestamp")
    val timestamp: String
)