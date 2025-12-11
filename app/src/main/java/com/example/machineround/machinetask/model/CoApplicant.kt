package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class CoApplicant(
    @SerializedName("dob")
    val dob: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("incomeMonthly")
    val incomeMonthly: Int,
    @SerializedName("kycStatus")
    val kycStatus: Boolean,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("occupation")
    val occupation: String,
    @SerializedName("relationship")
    val relationship: String
)