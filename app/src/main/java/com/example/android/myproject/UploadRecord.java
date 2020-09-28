package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadRecord extends AppCompatActivity {
    public EditText uDescription, utitle;
    String key,CurrentUser;
    DatabaseReference dataref;
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_record);
        setTitle("Upload Record");
        uDescription = findViewById(R.id.uploadDescription2);
        utitle = findViewById(R.id.uploadTitle2);
        btnUpload = findViewById(R.id.btnUpload2);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            key = mBundle.getString("keyValue");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }

        dataref = FirebaseDatabase.getInstance().getReference("Records").child(CurrentUser).child(key);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();

            }
        });
    }

    private void upload() {
        String Title = utitle.getText().toString();
        String  description = uDescription.getText().toString();

        if (TextUtils.isEmpty(Title)) {
            Toast.makeText(UploadRecord.this, "Enter the title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(UploadRecord.this, "Enter the Description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(Title) && !TextUtils.isEmpty(description)) {
            String id = dataref.push().getKey();
            Record myRecord = new Record(
                    uDescription.getText().toString(),
                    utitle.getText().toString(),
                    key,
                    id
            );
            dataref.child(id).setValue(myRecord);
            Toast.makeText(this, "Record uploaded Successfully", Toast.LENGTH_SHORT).show();
            nxtActivity();
        } else
            Toast.makeText(this, "Write title and description !", Toast.LENGTH_SHORT).show();
    }

    private void nxtActivity() {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("Records", 2);
        startActivity(intent);
        finish();
    }
}

