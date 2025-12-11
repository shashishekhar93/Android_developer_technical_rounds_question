package com.example.machineround.machinetask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.machineround.R
import com.example.machineround.databinding.FragmentVehicleInspectionBinding
import com.example.machineround.machinetask.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VehicleInspection : Fragment() {

    private lateinit var binding: FragmentVehicleInspectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVehicleInspectionBinding.inflate(inflater, container, false)
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


        binding.vehicleType.text = "Vehicle Name :- "+users.data.vehicle.type
        binding.vehicleMaker.text = "Maker :- "+users.data.vehicle.make
        binding.model.text = "Model :- "+users.data.vehicle.model
        binding.regNumber.text = "Registration No :- "+users.data.vehicle.registrationNumber
        binding.regDate.text = "Registration Date :- "+users.data.vehicle.registrationDate
        binding.inspectionStatus.text = "Inspection Status :- "+users.data.vehicle.inspection.status

        for (image in 0..users.data.vehicle.inspection.images.size) {
            Glide.with(this)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher_round)
                .override(200, 200)
                .load(image).into(binding.vehicleImages)
        }

    }
}