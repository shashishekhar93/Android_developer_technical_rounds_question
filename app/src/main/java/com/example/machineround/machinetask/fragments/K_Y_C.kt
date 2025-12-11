package com.example.machineround.machinetask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.machineround.R
import com.example.machineround.databinding.FragmentKYCBinding
import com.example.machineround.machinetask.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/*"kyc": {
        "aadhaar": {
          "number": "XXXX XXXX 2345",
          "verified": true,
          "imageFront": "https://ibb.co/G45tHNrf",
          "imageBack": "https://ibb.co/G45tHNrf"
        },
        "pan": {
          "number": "ABCDE1234F",
          "verified": true,
          "image": "https://ibb.co/G45tHNrf"
        }
      }
      */
class K_Y_C : Fragment() {
    private lateinit var binding: FragmentKYCBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKYCBinding.inflate(inflater, container, false)
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

        binding.adharNumber.text = users.data.memberDetails.kyc.aadhaar.number
        binding.adharVerificationStatus.text =
            users.data.memberDetails.kyc.aadhaar.verified.toString()
        Glide.with(view.context).load(users.data.memberDetails.kyc.aadhaar.imageFront)
            .placeholder(R.mipmap.ic_launcher_round)
            .override(200, 200)
            .into(binding.imageFront)
        Glide.with(view.context).load(users.data.memberDetails.kyc.aadhaar.imageBack)
            .placeholder(R.mipmap.ic_launcher_round)
            .override(200, 200)
            .into(binding.imageBack)

        binding.panNumber.text = users.data.memberDetails.kyc.pan.number
        binding.panverificationStatus.text = users.data.memberDetails.kyc.pan.verified.toString()
        Glide.with(view.context).load(users.data.memberDetails.kyc.pan.image)
            .placeholder(R.mipmap.ic_launcher_round)
            .override(200, 200)
            .into(binding.panimageFront)


    }
}