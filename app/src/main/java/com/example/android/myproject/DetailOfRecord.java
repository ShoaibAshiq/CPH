package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailOfRecord extends AppCompatActivity {


    EditText title, description;
    String id,Userkey;
    DatabaseReference databaseReference;
    String CurrentUser;
    String mtitle,mDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_record);
        getSupportActionBar().setTitle("Detail");

        title = findViewById(R.id.dRTitle);
        description = findViewById(R.id.dRDescription);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            title.setText(mBundle.getString("title"));
            description.setText(mBundle.getString("descrip"));
            id = mBundle.getString("key");
            Userkey = mBundle.getString("userkey");
        }
        Toast.makeText(DetailOfRecord.this, id, Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Records").child(CurrentUser).child(Userkey);
    }
    private void nxtActivity() {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("Records", 2);
        startActivity(intent);
        finish();
    }

    public void btnDeleteRecord(View view) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Records").child(CurrentUser).child(Userkey);
        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailOfRecord.this, "Deleted Successfully ", Toast.LENGTH_SHORT).show();
                nxtActivity();
            }
        });
    }

    public void btnUpdateRecord(View view) {
        mtitle = title.getText().toString().trim();
        mDescription = description.getText().toString().trim();
        uplaodData();
    }
    public void uplaodData() {

        Record myRecord = new Record(
                mDescription,
                mtitle,
                Userkey,
                id
        );
        databaseReference.child(id).setValue(myRecord);
        Toast.makeText(DetailOfRecord.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
        nxtActivity();
    }
}
