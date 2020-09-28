package com.example.android.myproject.api;

import com.example.android.myproject.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
     Call<News> getNews(
            @Query("country") String country,
            @Query("apikey") String apikey,
            @Query("category") String category
    );
}
