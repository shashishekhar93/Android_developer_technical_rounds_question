package com.example.machineround.machinetask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.machineround.R
import com.example.machineround.databinding.FragmentLoanSummeryBinding
import com.example.machineround.machinetask.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoanSummery : Fragment() {
    private lateinit var binding: FragmentLoanSummeryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoanSummeryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonString = resources.openRawResource(R.raw.sampledata)
            .bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val users: ResponseData =
            gson.fromJson(jsonString, object : TypeToken<ResponseData>() {}.type)


        binding.tvLoanId.text = "Loan ID :- " + users.data.loan.loanId
        binding.tvProduct.text = "Product :- " + users.data.loan.product
        binding.tvamountRequested.text =
            "Amount Requested :- " + users.data.loan.amountRequested.toString()
        binding.tvamountApproved.text =
            "Amount Approved :- " + users.data.loan.amountApproved.toString()
        binding.tvinterestRate.text = "Interest Rate :- " + users.data.loan.interestRate.toString()
        binding.tvtenure.text = "Tenure :- " + users.data.loan.tenure.toString()
        binding.tvstatus.text = "Status :- " + users.data.loan.status
        binding.tvsubmittedAt.text = "Submitted At :- " + users.data.loan.submittedAt

        for (coApp in users.data.loan.coApplicants) {
            binding.coApplicantId.text = "Co-Applicant ID :- " + coApp.id
            binding.coApplicantName.text = "Co-Applicant Name :- " + coApp.name
            binding.coRelationship.text = "Co-Applicant Relationship :- " + coApp.relationship
            binding.coDob.text = "Co-Applicant DOB :- " + coApp.dob

            binding.coMobile.text =
                "Co-Applicant Mobile :- " + coApp.mobile
            binding.occupation.text =
                "Co-Applicant Occupation :- " + coApp.occupation
            binding.monthlyIncome.text =
                "Co-Applicant Monthly Income :- " + coApp.incomeMonthly
            binding.kycStatus.text = "Co-Applicant Kyc Status :- "
            if (coApp.kycStatus) {
                binding.kycStatus.text = "KYC Verified"
                binding.kycStatus.setTextColor(resources.getColor(R.color.green))
            } else {
                binding.kycStatus.text = "KYC Not Verified"
                binding.kycStatus.setTextColor(resources.getColor(R.color.red))
            }
        }

        for (guarantor in users.data.loan.guarantors) {
            binding.guarantorID.text =
                "Guarantor ID :- " + guarantor.id
            binding.guarantorName.text =
                "Guarantor Name :- " + guarantor.name
            binding.relationship.text =
                "Guarantor Relationship :- " + guarantor.relationship
            if (guarantor.verified) {
                binding.guaVarified.text = "Guarantor Verified"
                binding.guaVarified.setTextColor(resources.getColor(R.color.green))
                binding.guaStatus.text =
                    "Check Status :- " + guarantor.checkStatus
                binding.guaStatus.setTextColor(resources.getColor(R.color.green))
            } else {
                binding.guaVarified.text = "Guarantor Not Verified"
                binding.guaVarified.setTextColor(resources.getColor(R.color.red))
                binding.guaStatus.text =
                    "Check Status :- " + guarantor.checkStatus
                binding.guaStatus.setTextColor(resources.getColor(R.color.red))
            }
        }

        for (emi in users.data.loan.emis) {
            "EMI Schedule :- " + emi.schedule
            binding.emiAmount.text = "EMI Amount :- " + emi.amount
            binding.emiStatus.text = "EMI Status :- " + emi.status
            binding.emiStatus.setTextColor(resources.getColor(R.color.green))
            if (emi.status == "Upcoming") {
                binding.emiStatus.setTextColor(resources.getColor(R.color.orange))
                binding.emiStatus.text =
                    "EMI Status :- " + emi.status
            } else {
                binding.emiStatus.setTextColor(resources.getColor(R.color.green))
                binding.emiStatus.text =
                    "EMI Status :- " + emi.status
            }
        }
    }
}