package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    EditText fore;
    Button reset;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        fore=findViewById(R.id.email);
        getSupportActionBar().setTitle("Reset Password");
        reset=findViewById(R.id.reset_btn);
        firebaseAuth= FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= fore.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(ForgetActivity.this, "Enter your Registered Email", Toast.LENGTH_SHORT).show();

                }
                else {
                    firebaseAuth.sendPasswordResetEmail(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                                finish();
                            }

                            else{
                                Toast.makeText(ForgetActivity.this, "Error in Sending Password", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
                }

            }
        });
    }
}
