package com.example.machineround.machinetask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.machineround.R
import com.example.machineround.databinding.FragmentProfileBinding
import com.example.machineround.machinetask.model.ResponseData
import com.example.machineround.machinetask.utils.readJSONFromAssets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonString = resources.openRawResource(R.raw.sampledata)
            .bufferedReader()
            .use { it.readText() }

        //val jsonFileString = readJSONFromAssets(binding.root.context, "sampledata.json")
        val gson = Gson()
        val users: ResponseData = gson.fromJson(jsonString, object : TypeToken<ResponseData>() {}.type)

        binding.memberId.text ="Member ID = "+ users.data.memberDetails.memberId
        binding.fullname.text = "Full Name = "+users.data.memberDetails.fullName
        binding.dob.text = "Date Of Birth = "+users.data.memberDetails.dob
        binding.mobile.text = "Mobile = "+users.data.memberDetails.mobile
        binding.email.text = "Email = "+users.data.memberDetails.email
        binding.maritalStatus.text = "Marital Status = "+users.data.memberDetails.maritalStatus
        binding.gender.text ="Gender = "+ users.data.memberDetails.gender

    }
}