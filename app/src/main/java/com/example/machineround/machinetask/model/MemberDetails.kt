package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class MemberDetails(
    @SerializedName("addresses")
    val addresses: List<Addresse>,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("kyc")
    val kyc: Kyc,
    @SerializedName("maritalStatus")
    val maritalStatus: String,
    @SerializedName("memberId")
    val memberId: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("type")
    val type: String
)