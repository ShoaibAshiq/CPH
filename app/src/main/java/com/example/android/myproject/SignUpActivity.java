package com.example.android.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    String gender = "";
    private EditText inputName, inputEmail, inputPhoneNumber, inputPassword, txtcofirmpass;
    RadioButton radioGenderMale, radioGenderFemale;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private Toolbar toolbar;
    String status = "active";

    private static final Pattern phone_PATTERN =
            Pattern.compile(".{11}");

    private static final Pattern Usernmae_PATTERN =
            Pattern.compile(".{3,}");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up");


        CreateAccountButton = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.register_username_input);
        inputEmail = findViewById(R.id.register_email_input);
        inputPassword = findViewById(R.id.register_password_input);
        txtcofirmpass = findViewById(R.id.con);
        inputPhoneNumber = findViewById(R.id.register_phone_number_input);
        radioGenderMale = findViewById(R.id.txt_radiomale);
        radioGenderFemale = findViewById(R.id.txt_radiofemale);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String pass = inputPassword.getText().toString();
                final String phone = inputPhoneNumber.getText().toString();
                String conf = txtcofirmpass.getText().toString();


                if (radioGenderMale.isChecked()) {
                    gender = "Male";
                }
                if ((radioGenderFemale.isChecked())) {
                    gender = "Female";
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Usernmae_PATTERN.matcher(username).matches()){
                    Toast.makeText(SignUpActivity.this, "At least 3 character are required for username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(conf)) {
                    Toast.makeText(SignUpActivity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SignUpActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phone_PATTERN.matcher(phone).matches()){
                    Toast.makeText(SignUpActivity.this, "invalid number ! Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!PASSWORD_PATTERN.matcher(pass).matches()) {
                    Toast.makeText(SignUpActivity.this, "Required 6 digits combination of Alphabets(one Upper Case) and Numbers ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(gender)){
                    Toast.makeText(SignUpActivity.this, "Please Select the gender !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.equals(conf) && PASSWORD_PATTERN.matcher(pass).matches()) {
                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setTitle("Creating Account !");
                    progressDialog.setMessage("Please Wait ...");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(SignUpActivity.this, "Email Verification sent. Please check your Email !", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Users user = new Users(
                                                inputName.getText().toString(),
                                                inputEmail.getText().toString(),
                                                inputPhoneNumber.getText().toString(),
                                                gender,
                                                status
                                                );
                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                                startActivity(intent);
                                                inputName.setText("");
                                                inputEmail.setText("");
                                                inputPhoneNumber.setText("");
                                                txtcofirmpass.setText("");
                                                inputPassword.setText("");
                                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                finish();
                                            }
                                        });

                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}

