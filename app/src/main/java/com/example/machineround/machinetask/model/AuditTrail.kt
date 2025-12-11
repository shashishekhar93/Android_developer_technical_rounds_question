package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class AuditTrail(
    @SerializedName("action")
    val action: String,
    @SerializedName("actor")
    val actor: String,
    @SerializedName("timestamp")
    val timestamp: String
)