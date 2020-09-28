package com.example.android.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;


public class ChildDataActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager view;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);
        toolbar = findViewById(R.id.myToolbar);
        tabLayout = findViewById(R.id.tablayout);
        view = findViewById(R.id.myViewPager);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            key = mBundle.getString("keyValue");
        }
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
        if (getIntent().getIntExtra("Pictures", 0) == 1) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addfragment(new AddPictureFragment(), "Pictures");
            finish();
        }
        if (getIntent().getIntExtra("Records", 0) == 2) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addfragment(new AddPictureFragment(), "Records");
            finish();
        }
//        if (getIntent().getIntExtra("Vaccine Reminders", 0) == 3) {
//            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//            viewPagerAdapter.addfragment(new AddPictureFragment(), "Vaccine Reminders");
//            finish();
//        }
        setSupportActionBar(toolbar);
        setupViewPager(view);
        tabLayout.setupWithViewPager(view);
    }

    public String getMyData() {
        return key;
    }

    private void setupViewPager(ViewPager v) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addfragment(new AddPictureFragment(), "Pictures");
        viewPagerAdapter.addfragment(new AddRecordFragment(), "Record");
        viewPagerAdapter.addfragment(new GraphFragment(), "VR");
        v.setAdapter(viewPagerAdapter);
    }
}
