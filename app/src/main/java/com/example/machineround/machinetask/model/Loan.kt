package com.example.machineround.machinetask.model


import com.google.gson.annotations.SerializedName

data class Loan(
    @SerializedName("amountApproved")
    val amountApproved: Any,
    @SerializedName("amountRequested")
    val amountRequested: Double,
    @SerializedName("coApplicants")
    val coApplicants: List<CoApplicant>,
    @SerializedName("emis")
    val emis: List<Emi>,
    @SerializedName("guarantors")
    val guarantors: List<Guarantor>,
    @SerializedName("interestRate")
    val interestRate: Double,
    @SerializedName("loanId")
    val loanId: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("referenceContacts")
    val referenceContacts: List<ReferenceContact>,
    @SerializedName("status")
    val status: String,
    @SerializedName("submittedAt")
    val submittedAt: String,
    @SerializedName("tenure")
    val tenure: Int
)