package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    String key;
    private LinearLayout AddChildBtn,signOutBtn,ChildViewbtn,NewsfeedBtn,ReminderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home");

        AddChildBtn = findViewById(R.id.AddNewChild_btn);
        ChildViewbtn = findViewById(R.id.childernView);
        NewsfeedBtn = findViewById(R.id.NewsFeed_btn);
        signOutBtn = findViewById(R.id.signout);
        ReminderBtn = findViewById(R.id.Reminder_btn);

        AddChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AddChildActivity.class);
                startActivity(intent);

            }
        });

        ChildViewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ChildViewActivity.class);
                startActivity(intent);
            }
        });

        NewsfeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Newsfeed.class);
                startActivity(intent);

            }
        });
        ReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ReminderActivity.class);
                startActivity(intent);

            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
              FirebaseAuth fAuth = FirebaseAuth.getInstance();
              fAuth.signOut();
                startActivity(intent);
                finish();
            }
        });

    }
 }

