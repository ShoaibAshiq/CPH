package com.example.android.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPicture extends AppCompatActivity {

    ImageView image;
    Uri uri;
    String imageUrl;
    EditText uDescription, utitle;
    String  CurrentUser;
    String key;
    DatabaseReference databaseChildData;
    Button btnupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        setTitle("Upload Image");

        image = findViewById(R.id.uploadImage);
        uDescription = findViewById(R.id.uploadDescription);
        utitle = findViewById(R.id.uploadTitle);
        btnupload = findViewById(R.id.btnUpload);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            key = mBundle.getString("keyValue");
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             CurrentUser = user.getUid();
        }

        databaseChildData = FirebaseDatabase.getInstance().getReference("Pictures").child(CurrentUser).child(key);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplaodData();

            }
        });
    }

    public void btnSelectImage(View view) {
        Intent photopicker = new Intent(Intent.ACTION_PICK);
        photopicker.setType("image/*");
        startActivityForResult(photopicker, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            image.setImageURI(uri);
            uploadImage();
        } else
            Toast.makeText(this, "You have not selected a photo", Toast.LENGTH_SHORT).show();
    }

    public void uploadImage() {

        StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference().child("Image").child(uri.getLastPathSegment());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading ...");
                progressDialog.show();

                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlImage = uriTask.getResult();
                        imageUrl = urlImage.toString();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }


    public void uplaodData() {

        if (!TextUtils.isEmpty(imageUrl)) {
            String id = databaseChildData.push().getKey();
            Record myRecord = new Record(
                    uDescription.getText().toString(),
                    imageUrl,
                    utitle.getText().toString(),
                    key,
                    id
            );
            databaseChildData.child(id).setValue(myRecord);
            Toast.makeText(this, "Uploaded Sucessfully", Toast.LENGTH_SHORT).show();
            nxtActivity();
        } else
            Toast.makeText(this, "Please Select img ! ", Toast.LENGTH_SHORT).show();
    }

    private void nxtActivity() {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("Pictures", 1);
        startActivity(intent);
        finish();
    }
}
