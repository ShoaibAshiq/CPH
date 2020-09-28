package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.myproject.api.ApiClient;
import com.example.android.myproject.api.ApiInterface;
import com.example.android.myproject.models.Article;
import com.example.android.myproject.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Newsfeed extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public  static final String API_KEY = "2de2c8ea260648e09e7c12845f75e290";
    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>() ;
    private NewsFeedAdapter adapter;
    private String TAG = Newsfeed.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    Context context ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_newsfeed);
        getSupportActionBar().setTitle("Newsfeed");

        recyclerView = findViewById(R.id.Newsfeed_RecyclerView);
        layoutManager = new LinearLayoutManager(Newsfeed.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new NewsFeedAdapter(articles,Newsfeed.this);
        recyclerView.setAdapter(adapter);
//        LoadJson();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        onLoadingSwipeRefresh();
    }
    public void LoadJson(){
        swipeRefreshLayout.setRefreshing(true);
        new ApiClient();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = "us";
        String category = "health";

        Call<News> call;
        call = apiInterface.getNews(country,API_KEY,category);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    adapter = new NewsFeedAdapter(articles,Newsfeed.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(Newsfeed.this, "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }
    private void initListener(){
        adapter.setOnItemClickListener(new NewsFeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Newsfeed.this,NewsDetailActivity.class);
                Article article = articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        LoadJson();
    }
    private void onLoadingSwipeRefresh(){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson();
                    }
                }
        );
    }
}