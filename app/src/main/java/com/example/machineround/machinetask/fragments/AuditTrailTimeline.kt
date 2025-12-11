package com.example.machineround.machinetask.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.machineround.R
import com.example.machineround.databinding.FragmentAuditTrailTimelineBinding
import com.example.machineround.databinding.FragmentKYCBinding
import com.example.machineround.machinetask.fragments.Documents.DocumentAdapter
import com.example.machineround.machinetask.model.AuditTrail
import com.example.machineround.machinetask.model.Document
import com.example.machineround.machinetask.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuditTrailTimeline : Fragment() {

    /*
    * "auditTrail": [
      {
        "action": "APP_CREATED",
        "actor": "Agent",
        "timestamp": "2025-02-11 13:15:20"
      },
      {
        "action": "KYC_VERIFIED",
        "actor": "System",
        "timestamp": "2025-02-15 09:20:21"
      },
      {
        "action": "INSPECTION_INITIATED",
        "actor": "System",
        "timestamp": "2025-02-15 10:10:10"
      }
    ]
    * */

    private lateinit var binding: FragmentAuditTrailTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuditTrailTimelineBinding.inflate(inflater, container, false)
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

        binding.trailRv.layoutManager = LinearLayoutManager(view.context)
        binding.trailRv.adapter = TrailAdapter(users.data.auditTrail)
    }




    class TrailAdapter(private var listItems: List<AuditTrail>) :
        RecyclerView.Adapter<TrailViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): TrailViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trail, parent, false)
            return TrailViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: TrailViewHolder,
            position: Int
        ) {
            val trail = listItems[position]
            holder.action.text = trail.action +" By "+ trail.actor
            holder.timestamp.text = trail.timestamp
            if (position == itemCount-1){
                holder.itemLine.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return listItems.size
        }

    }


    class TrailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val action: TextView = view.findViewById(R.id.trailText)
        val timestamp: TextView = view.findViewById(R.id.timeStampTv)

        val itemLine:View = view.findViewById(R.id.viewline)
    }


}