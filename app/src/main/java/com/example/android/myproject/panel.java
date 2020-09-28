package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class panel extends AppCompatActivity {
    String key;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    RecyclerView mRecyclerView;
    List<Users> usersList;
    UserAdapter myAdapter = new UserAdapter(panel.this, usersList);
    EditText txt_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        getSupportActionBar().setTitle("Admin Panel");

        mRecyclerView = findViewById(R.id.AdminPanelrecyclerView);
        txt_Search = findViewById(R.id.AdminPanelsearch);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(panel.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        usersList = new ArrayList<>();
        myAdapter = new UserAdapter(panel.this, usersList);
        mRecyclerView.setAdapter(myAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            key = user.getUid();
            Toast.makeText(this,key, Toast.LENGTH_SHORT).show();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Users user1 = itemSnapshot.getValue(Users.class);
                    user1.setKey(itemSnapshot.getKey());
                    usersList.add(user1);
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
        ArrayList<Users> filterList = new ArrayList<>();
        for (Users item : usersList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }
        myAdapter.filteredList(filterList);
    }
}
