package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Kyc(
    @SerializedName("aadhaar")
    val aadhaar: Aadhaar,
    @SerializedName("pan")
    val pan: Pan
)