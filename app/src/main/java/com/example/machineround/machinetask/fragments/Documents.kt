package com.example.machineround.machinetask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.machineround.R
import com.example.machineround.databinding.FragmentDocumentsBinding
import com.example.machineround.machinetask.model.Document
import com.example.machineround.machinetask.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*"documents": [
      {
        "docType": "Bank Statement",
        "fileUrl": "https://ibb.co/G45tHNrf",
        "uploadedAt": "2025-02-12 11:45:20"
      },
      {
        "docType": "Salary Slip",
        "fileUrl": "https://ibb.co/G45tHNrf",
        "uploadedAt": "2025-02-12 11:50:20"
      }
    ]*/

class Documents : Fragment() {
    private lateinit var binding: FragmentDocumentsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)
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

        binding.documentRv.layoutManager = LinearLayoutManager(view.context)
        binding.documentRv.adapter = DocumentAdapter(users.data.documents)


//        typefor (document in users.data.documents) {
//            binding.docType.text = document.docType
//            binding.fileUrl.text= document.fileUrl
//            binding.uploadedAt.text= document.uploadedAt
//        }

    }

    class DocumentAdapter(private var listItems: List<Document>) :
        RecyclerView.Adapter<DocumentViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DocumentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.document_list_item, parent, false)
            return DocumentViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: DocumentViewHolder,
            position: Int
        ) {
            val document = listItems[position]
            holder.docType.text = document.docType
            holder.fileUrl.text = document.fileUrl
            holder.uploadedAt.text = document.uploadedAt
        }

        override fun getItemCount(): Int {
            return listItems.size
        }

    }


    class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val docType: TextView = view.findViewById(R.id.doc_type)
        val fileUrl: TextView = view.findViewById(R.id.file_url)
        val uploadedAt: TextView = view.findViewById(R.id.uploaded_at)
    }
}