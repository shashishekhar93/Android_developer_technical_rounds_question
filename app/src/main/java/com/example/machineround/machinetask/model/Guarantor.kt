package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Guarantor(
    @SerializedName("checkStatus")
    val checkStatus: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("relationship")
    val relationship: String,
    @SerializedName("verified")
    val verified: Boolean
)