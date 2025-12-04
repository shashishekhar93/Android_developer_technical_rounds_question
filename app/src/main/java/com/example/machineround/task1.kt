package com.example.machineround

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//model classes
data class RandomUserResponse(val results: List<ContactDto>, val info: InfoObj)
data class InfoObj(val seed: String, val results: Int, val page: Int, val version: String)
data class ContactDto(val name: NameDto, val phone: String, val picture: PictureDto)
data class NameDto(val title: String, val first: String, val last: String)
data class PictureDto(val large: String, val medium: String, val thumbnail: String)

//remote interface
interface RandomUserApi {
    @GET("api/")
    suspend fun getContacts(
        @Query("page") page: Int = 1,
        @Query("results") results: Int = 20,
        @Query("inc") inc: String = "name,phone,picture",
        @Query("nat") nat: String = "us,gb,ca,au",
    ): RandomUserResponse
}

//retrofit instance
private const val BASE_URL = "https://randomuser.me/"

private fun createOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY   // BASIC / HEADERS / BODY / NONE
    }

    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(createOkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: RandomUserApi = retrofit.create(RandomUserApi::class.java)

//recyclerview
class ContactsAdapter(private var items: List<ContactDto>) :
    RecyclerView.Adapter<ContactsRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactsRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContactsRvViewHolder, position: Int) {
        val contact = items[position]
        holder.tvName.text =
            contact.name.title + ". " + contact.name.first + " " + contact.name.last

        holder.tvPhone.text = contact.phone
        Glide.with(holder.itemView).load(contact.picture.medium)
            .into(holder.ivPic)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ContactDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}

//viewholder for recyclerview.
class ContactsRvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPic: ImageView = view.findViewById(R.id.ivAvatar)
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvPhone: TextView = view.findViewById(R.id.tvPhone)
}


class ListViewModel : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactDto>>()
    val contacts: LiveData<List<ContactDto>> get() = _contacts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    private var page = 1
    var isLastPage = false
        private set

    init {
        loadMoreContacts()
    }

    fun loadMoreContacts() {
        if (_isLoading.value == true || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null
            try {
                val response = api.getContacts(page)
                val newContacts = response.results
                if (newContacts.isEmpty()) {
                    isLastPage = true
                } else {
                    val mappedValue = newContacts.map {
                        ContactDto(
                            name = NameDto(it.name.title, it.name.first, it.name.last),
                            phone = it.phone,
                            picture = it.picture
                        )
                    }
                    val currentContacts = _contacts.value ?: emptyList()
                    _contacts.value = currentContacts + mappedValue
                    page++
                }
            } catch (e: Exception) {
                _isError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class Task1Activity : ComponentActivity() {

    private lateinit var contactViewModel: ListViewModel
    private lateinit var adapter: ContactsAdapter

    private var isScrolling = false

    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task1)
        progressbar = findViewById<ProgressBar>(R.id.progressbar)
        initRecyclerView()
        contactViewModel = ViewModelProvider(this)[ListViewModel::class.java]

        contactViewModel.contacts.observe(this) { contacts ->
            adapter.submitList(contacts)
        }

        contactViewModel.isLoading.observe(this) { loading ->
            if (loading) showProgressBar() else hideProgressBar()
        }

        contactViewModel.isError.observe(this) { error ->
            hideProgressBar()
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.contact_rv)
        adapter = ContactsAdapter(emptyList())
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        recyclerview.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isLoading = contactViewModel.isLoading.value ?: false
            val isLastPage = contactViewModel.isLastPage

            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val shouldPaginate = !isLoading && !isLastPage && isAtLastItem && isNotAtBeginning && isScrolling

            if (shouldPaginate) {
                contactViewModel.loadMoreContacts()
                isScrolling = false
            }
        }
    }
    private fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        progressbar.visibility = View.GONE
    }
}