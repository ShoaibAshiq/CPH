package com.example.android.myproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class AddChildActivity extends AppCompatActivity {
    String key;
    String gender = "";
    EditText txt_ChildName;
    TextView tXt_age;
    public static TextView txt_dob;
    Button btn_register, btn_datePickbtn;
    RadioButton radioGenderMale, radioGenderFemale;
    DatabaseReference databaseReference;
    Child addChild;
    private static final Pattern Childname_PATTERN =
            Pattern.compile(".{3,}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        getSupportActionBar().setTitle("Add Child");

        txt_ChildName = findViewById(R.id.txt_name);
        txt_dob = findViewById(R.id.child_dob);
//        tXt_age = findViewById(R.id.child_Age);
        radioGenderMale = findViewById(R.id.txt_radiomale);
        radioGenderFemale = findViewById(R.id.txt_radiofemale);
        btn_register = findViewById(R.id.button1);
        btn_datePickbtn = findViewById(R.id.datePickbtn);

        btn_datePickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), datePickListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        addChild = new Child();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ChildName = txt_ChildName.getText().toString().trim();
                String ChildDob = txt_dob.getText().toString().trim();
//                String ChildAge = tXt_age.getText().toString().trim();

                if (radioGenderMale.isChecked()) {
                    gender = "Male";
                }
                if (radioGenderFemale.isChecked()) {
                    gender = "Female";
                }

                if (TextUtils.isEmpty(ChildName)) {
                    Toast.makeText(AddChildActivity.this, "Please Enter the Name ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Childname_PATTERN.matcher(ChildName).matches()){
                    Toast.makeText(AddChildActivity.this, "At least 3 character are required for name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ChildDob)) {
                    Toast.makeText(AddChildActivity.this, "Please Enter the DOB ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(AddChildActivity.this, "Select a Gender ", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    key = user.getUid();
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Childs").child(key);

                if (!TextUtils.isEmpty(ChildName) && !TextUtils.isEmpty(ChildDob) && !TextUtils.isEmpty(gender))
                {
                    String id = databaseReference.push().getKey();
                    Child addChild = new Child();
                    addChild.setName(ChildName);
                    addChild.setGender(gender);
                    addChild.setDob(ChildDob);
//                    addChild.setAge(ChildAge);
                    assert id != null;
                    databaseReference.child(id).setValue(addChild);
                    Toast.makeText(AddChildActivity.this, "Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddChildActivity.this, ChildViewActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(AddChildActivity.this, "Failed . Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String format = new SimpleDateFormat("dd/MM/YYYY").format(calendar.getTime());
            txt_dob.setText(format);
//            tXt_age.setText(Integer.toString(calculateAge(calendar.getTimeInMillis())));
        }
    };

//    int calculateAge(long date) {
//        Calendar dob = Calendar.getInstance();
//        dob.setTimeInMillis(date);
//        Calendar today = Calendar.getInstance();
//        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
//            age--;
//        }
//
//        return age;
//    }


}

