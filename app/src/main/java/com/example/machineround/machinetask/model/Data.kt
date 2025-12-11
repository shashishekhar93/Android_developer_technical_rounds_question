package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("applicationId")
    val applicationId: String,
    @SerializedName("auditTrail")
    val auditTrail: List<AuditTrail>,
    @SerializedName("documents")
    val documents: List<Document>,
    @SerializedName("loan")
    val loan: Loan,
    @SerializedName("memberDetails")
    val memberDetails: MemberDetails,
    @SerializedName("vehicle")
    val vehicle: Vehicle
)