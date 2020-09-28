package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserDetail extends AppCompatActivity {

    String key, status, name, email, phone, gender;
    TextView uName, CurrentStatus;
    Button activate, deactivate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        uName = findViewById(R.id.user);
        CurrentStatus = findViewById(R.id.status);
        activate = findViewById(R.id.StatusActive);
        deactivate = findViewById(R.id.StatusDeActive);

        getSupportActionBar().setTitle("Detail");


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            uName.setText(mBundle.getString("username"));
            email = mBundle.getString("email");
            phone = mBundle.getString("phone");
            gender = mBundle.getString("gender");
            CurrentStatus.setText(mBundle.getString("status"));
            status = mBundle.getString("status");
            key = mBundle.getString("key");
        }


        Toast.makeText(UserDetail.this, key, Toast.LENGTH_SHORT).show();

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateAccount();
            }
        });
        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeactivateAccount();
            }
        });


    }

    private void DeactivateAccount() {

        status = CurrentStatus.getText().toString().trim();
        name = uName.getText().toString().trim();
        if (status.equals("DeActive")) {
            Toast.makeText(this, "Account is already in DeActive status !", Toast.LENGTH_SHORT).show();
            finish();
        }else {
           status = "DeActive";

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                    databaseReference.child(key).child("status").setValue(status);
                    Intent intent = new Intent(UserDetail.this, panel.class);
                    Toast.makeText(UserDetail.this, "This account is now Deactivated Status", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }

        }




    private void activateAccount() {
        status = CurrentStatus.getText().toString().trim();
        name = uName.getText().toString().trim();
        if (status.equals("active")) {
            Toast.makeText(this, "Account is already in Active status !", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            status = "active";

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.child(key).child("status").setValue(status);
            Intent intent = new Intent(UserDetail.this, panel.class);
            Toast.makeText(UserDetail.this, "This account is now Activated Status", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();

        }
    }

}

