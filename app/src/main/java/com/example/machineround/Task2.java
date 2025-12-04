package com.example.machineround;

/*we are going to build a screen using volley, list view and base adapter class.
 * it is conventional code practice which was asked to me by interviewer in vervemobi blr*/

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

class UserResponse {
    @SerializedName("results")
    private List<Contact> results;

    @SerializedName("info")
    private Info info;

    // Getters & Setters
    public List<Contact> getResults() {
        return results;
    }

    public void setResults(List<Contact> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}

class Contact {
    @SerializedName("name")
    private Name name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("picture")
    private Picture picture;

    // Getters & Setters
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}

class Name {
    @SerializedName("title")
    private String title;

    @SerializedName("first")
    private String first;

    @SerializedName("last")
    private String last;

    // Getters & Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}

class Picture {
    @SerializedName("large")
    private String large;

    @SerializedName("medium")
    private String medium;

    @SerializedName("thumbnail")
    private String thumbnail;

    // Getters & Setters
    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

class Info {
    @SerializedName("seed")
    private String seed;

    @SerializedName("results")
    private int results;

    @SerializedName("page")
    private int page;

    @SerializedName("version")
    private String version;

    // Getters & Setters
    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}


interface RetrofitInterface {
    @GET("api/")
    Call<UserResponse> getUsers(
            @Query("page") int page,
            @Query("results") int results,
            @Query("inc") String inc,
            @Query("nat") String nat
    );
}

class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://randomuser.me/";

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RetrofitInterface getApiInterface() {
        return getRetrofitClient().create(RetrofitInterface.class);
    }
}

class ContactAdapter extends BaseAdapter {

    private final List<Contact> contactList;
    private final LayoutInflater layoutInflater;

    public ContactAdapter(Context context) {
        this.contactList = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void updateContactList(List<Contact> newList) {
        this.contactList.clear();
        this.contactList.addAll(newList);
        notifyDataSetChanged();
    }

    public void addContactsInList(List<Contact> newContact) {
        this.contactList.addAll(newContact);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_contact, parent, false);

            viewHolder = new BaseViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.ivAvatar);
            viewHolder.nameTextView = convertView.findViewById(R.id.tvName);
            viewHolder.phoneTextView = convertView.findViewById(R.id.tvPhone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }

        Contact contact = contactList.get(position);
        viewHolder.nameTextView.setText(contact.getName().getFirst() + " " + contact.getName().getLast());
        viewHolder.phoneTextView.setText(contact.getPhone());
        Glide.with(convertView).load(contact.getPicture().getMedium()).into(viewHolder.imageView);
        return convertView;
    }

    static class BaseViewHolder {
        ImageView imageView;
        TextView nameTextView, phoneTextView;
    }
}

public class Task2 extends ComponentActivity {

    ContactAdapter adapter;
    RetrofitInterface retrofitInterface;
    private ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        ListView listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        adapter = new ContactAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //check if we are at the bottom of the list.
                int totalItemCount = view.getCount();
                int firstVisibleItem = view.getFirstVisiblePosition();
                int visibleItemCount = view.getChildCount();
                if (!isLoading && !isLastPage) {
                    if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
                        loadContactsFromRemote();
                    }
                }
            }
        });
        retrofitInterface = RetrofitClient.getApiInterface();
        loadContactsFromRemote();
    }

    private void loadContactsFromRemote() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        Call<UserResponse> call = retrofitInterface.getUsers(currentPage, 20, "name,phone,picture", "us,gb,ca,au");
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Contact> users = response.body().getResults();
                    adapter.addContactsInList(users);

                    if (users != null && !users.isEmpty()) {
                        adapter.addContactsInList(users);
                        currentPage++;
                    } else {
                        isLastPage = true;
                    }
                }
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                isLoading = false;
                Log.e("TAG", "onFailure: ---", t.fillInStackTrace());
            }
        });
    }
}
