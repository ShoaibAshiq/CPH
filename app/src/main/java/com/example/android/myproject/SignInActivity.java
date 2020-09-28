package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    String CurrentUser;
    private EditText inputEmail, inputPassword;
    private Button LoginButton, AdminLoginButton;
    private TextView AdminLink, NotAdminLink, notMember, forget;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("Login");


        LoginButton = findViewById(R.id.login_btn);
        inputPassword = findViewById(R.id.login_password_input);
        inputEmail = findViewById(R.id.register_email_input);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        notMember = findViewById(R.id.notAMember);
        forget = findViewById(R.id.forget_password_link);
        AdminLoginButton = findViewById(R.id.loginAdmin_btn);
        firebaseAuth = FirebaseAuth.getInstance();


        NotAdminLink.setVisibility(View.INVISIBLE);
        AdminLoginButton.setVisibility(View.INVISIBLE);
        LoginButton.setVisibility(View.VISIBLE);
        AdminLink.setVisibility(View.VISIBLE);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                final String pass = inputPassword.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setTitle("Logging into Account !");
                progressDialog.setMessage("Please Wait ...");
                progressDialog.show();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Fill the login form !", Toast.LENGTH_SHORT).show();
                    return;
                } else {


                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (!user.isEmailVerified()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignInActivity.this, "Please Verify your Email First", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        progressDialog.dismiss();
                                        CheckStatus();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "User not found !", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetActivity.class));
                finish();
            }
        });
        notMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();

            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setVisibility(View.INVISIBLE);
                AdminLink.setVisibility(View.INVISIBLE);
                AdminLoginButton.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setVisibility(View.VISIBLE);
                AdminLink.setVisibility(View.VISIBLE);
                AdminLoginButton.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
            }
        });
        AdminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info();
            }
        });

    }

    public void CheckStatus() {

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Checking Status");
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUser);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dbStatus = dataSnapshot.child("status").getValue().toString();
                if (dbStatus.equals("active")) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, dbStatus, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Account is De-Activated ", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, "Login Error .Please Try Again", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void info() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");
        final String adEmail = inputEmail.getText().toString().trim();
        final String adPass = inputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(adEmail) && TextUtils.isEmpty(adPass)) {
            Toast.makeText(SignInActivity.this, "Fill the login form !", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setTitle("Login into Admin Account !");
            progressDialog.setMessage("Please Wait ...");
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String dbmail = dataSnapshot.child("email").getValue().toString();
                    String dbpass = dataSnapshot.child("password").getValue().toString();


                    if (adEmail.equals(dbmail) && adPass.equals(dbpass)) {

                        startActivity(new Intent(getApplicationContext(), panel.class));
                        inputEmail.setText("");
                        inputPassword.setText("");

                        Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignInActivity.this, "Login Error .Please Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

