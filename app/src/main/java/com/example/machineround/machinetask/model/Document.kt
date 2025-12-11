package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("docType")
    val docType: String,
    @SerializedName("fileUrl")
    val fileUrl: String,
    @SerializedName("uploadedAt")
    val uploadedAt: String
)