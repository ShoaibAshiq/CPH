package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Objects;

public class DetailOfPictures extends AppCompatActivity {

    EditText title, description;
    String id,Userkey;
    ImageView image;
    String imageUrl="",oldimageUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String CurrentUser;
    String mtitle,mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_pictures);
        getSupportActionBar().setTitle("Detail");

        title = findViewById(R.id.dpTitle);
        description = findViewById(R.id.dpDescription);
        image = findViewById(R.id.dpImage);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            Glide.with(this)
                    .load(mBundle.getString("image"))
                    .into(image);
            title.setText(mBundle.getString("title"));
            description.setText(mBundle.getString("descrip"));
            id = mBundle.getString("key");
            oldimageUrl = mBundle.getString("image");
            Userkey = mBundle.getString("userkey");
        }
//        storageReference = FirebaseStorage.getInstance()
//                .getReference().child("Image").child(uri.getLastPathSegment());
        Toast.makeText(DetailOfPictures.this, id, Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Pictures").child(CurrentUser).child(Userkey);

  }

    private void nxtActivity() {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("Pictures", 1);
        startActivity(intent);
        finish();
    }


    public void btnDeletePic(View view) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pictures").child(CurrentUser).child(Userkey);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(oldimageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(id).removeValue();
                Toast.makeText(DetailOfPictures.this, "Deleted Successfully ", Toast.LENGTH_SHORT).show();
                nxtActivity();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            Toast.makeText(DetailOfPictures.this, "You have not selected a photo", Toast.LENGTH_SHORT).show();
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



    public void btnUpdatePicture(View view) {
        mtitle = title.getText().toString().trim();
        mDescription = description.getText().toString().trim();
        uplaodData();


    }
    public void uplaodData() {
        if (!TextUtils.isEmpty(imageUrl)) {
            Record myRecord = new Record(
                    mDescription,
                    imageUrl,
                    mtitle,
                    Userkey,
                    id
            );
            databaseReference.child(id).setValue(myRecord);
            Toast.makeText(DetailOfPictures.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
            nxtActivity();
        }
        else if (TextUtils.isEmpty(imageUrl)){
            Record myRecord = new Record(
                    mDescription,
                    oldimageUrl,
                    mtitle,
                    Userkey,
                    id);
            databaseReference.child(id).setValue(myRecord);
            Toast.makeText(DetailOfPictures.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
            nxtActivity();
        }
        else
            Toast.makeText(DetailOfPictures.this, "Failed", Toast.LENGTH_SHORT).show();

}

}

