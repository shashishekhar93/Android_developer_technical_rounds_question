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

data class T3UserResponse(val results: List<T3ContactDto>, val info: T3InfoObj)
data class T3InfoObj(val seed: String, val results: Int, val page: Int, val version: String)
data class T3ContactDto(val name: T3NameDto, val phone: String, val picture: T3PictureDto)
data class T3NameDto(val title: String, val first: String, val last: String)
data class T3PictureDto(val large: String, val medium: String, val thumbnail: String)

//remote interface
interface T3UserApi {
    @GET("api/")
    suspend fun getContacts(
        @Query("page") page: Int = 1,
        @Query("results") results: Int = 1000,
        @Query("inc") inc: String = "name,phone,picture",
        @Query("nat") nat: String = "us,gb,ca,au",
    ): T3UserResponse
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

val T3retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(createOkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val T3api: T3UserApi = T3retrofit.create(T3UserApi::class.java)

//recyclerview
class T3ContactsAdapter(private var items: List<T3ContactDto>) :
    RecyclerView.Adapter<T3ContactsRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T3ContactsRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return T3ContactsRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: T3ContactsRvViewHolder, position: Int) {
        val T3contact = items[position]
        holder.tvName.text =
            T3contact.name.title + ". " + T3contact.name.first + " " + T3contact.name.last

        holder.tvPhone.text = T3contact.phone
        Glide.with(holder.itemView).load(T3contact.picture.medium)
            .into(holder.ivPic)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun t3submitList(newItems: List<T3ContactDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}

//view holder for recyclerview.
class T3ContactsRvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPic: ImageView = view.findViewById(R.id.ivAvatar)
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvPhone: TextView = view.findViewById(R.id.tvPhone)
}

//viewmodel for MVVM.
class T3ListViewModel : ViewModel() {

    private val _t3contacts = MutableLiveData<List<T3ContactDto>>()
    val t3contacts: LiveData<List<T3ContactDto>> get() = _t3contacts

    private val _t3isLoading = MutableLiveData<Boolean>()
    val t3isLoading: LiveData<Boolean> = _t3isLoading

    private val _t3isError = MutableLiveData<String?>()
    val t3isError: LiveData<String?> = _t3isError

    private var page = 1
    var t3isLastPage = false
        private set

    init {
        t3loadMoreContacts()
    }

    fun t3loadMoreContacts() {
        if (_t3isLoading.value == true || t3isLastPage) return

        viewModelScope.launch {
            _t3isLoading.value = true
            _t3isError.value = null
            try {
                val response = T3api.getContacts(page)
                val newContacts = response.results
                if (newContacts.isEmpty()) {
                    t3isLastPage = true
                } else {
                    val t3mappedValue = newContacts.map {
                        T3ContactDto(
                            name = T3NameDto(it.name.title, it.name.first, it.name.last),
                            phone = it.phone,
                            picture = it.picture
                        )
                    }
                    val t3currentContacts = _t3contacts.value ?: emptyList()
                    _t3contacts.value = t3currentContacts + t3mappedValue
                    page++
                }
            } catch (e: Exception) {
                _t3isError.value = e.message
            } finally {
                _t3isLoading.value = false
            }
        }
    }
}

class T3Activity : ComponentActivity() {

    private lateinit var t3contactViewModel: T3ListViewModel
    private lateinit var t3adapter: T3ContactsAdapter
    private var t3isScrolling = false
    private lateinit var t3progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task1)
        t3progressbar = findViewById<ProgressBar>(R.id.progressbar)
        initRecyclerView()
        t3contactViewModel = ViewModelProvider(this)[T3ListViewModel::class.java]

        t3contactViewModel.t3contacts.observe(this) { contacts ->
            t3adapter.t3submitList(contacts)
        }

        t3contactViewModel.t3isLoading.observe(this) { loading ->
            if (loading) showProgressBar() else hideProgressBar()
        }

        t3contactViewModel.t3isError.observe(this) { error ->
            hideProgressBar()
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.contact_rv)
        t3adapter = T3ContactsAdapter(emptyList())
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = t3adapter
        //recyclerview.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                t3isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isLoading = t3contactViewModel.t3isLoading.value ?: false
            val isLastPage = t3contactViewModel.t3isLastPage

            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val shouldPaginate =
                !isLoading && !isLastPage && isAtLastItem && isNotAtBeginning && t3isScrolling

            if (shouldPaginate) {
                t3contactViewModel.t3loadMoreContacts()
                t3isScrolling = false
            }
        }
    }

    private fun showProgressBar() {
        t3progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        t3progressbar.visibility = View.GONE
    }
}