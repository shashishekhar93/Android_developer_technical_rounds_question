package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class ReferenceContact(
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("relation")
    val relation: String
)