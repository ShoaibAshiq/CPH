package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildViewActivity extends AppCompatActivity {

    String key;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    RecyclerView mRecyclerView;
    List<Child> childList;
    MyAdapter myAdapter = new MyAdapter(ChildViewActivity.this, childList);
    EditText txt_Search;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

        getSupportActionBar().setTitle("Childrens");

        mRecyclerView = findViewById(R.id.recyclerView);
        txt_Search = findViewById(R.id.txt_searchtext);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ChildViewActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        childList = new ArrayList<>();
        myAdapter = new MyAdapter(ChildViewActivity.this, childList);
        mRecyclerView.setAdapter(myAdapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            key = user.getUid();
            Toast.makeText(this,key, Toast.LENGTH_SHORT).show();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Childs").child(key);
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Child addChild1 = itemSnapshot.getValue(Child.class);
                    addChild1.setKey(itemSnapshot.getKey());
                    childList.add(addChild1);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<Child> filterList = new ArrayList<>();
        for (Child item : childList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }
        myAdapter.filteredList(filterList);
    }
}

