package com.myapps.majorprojectversion5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Publish publish = new Publish();
    Profile profile = new Profile();
    Viewer viewer = new Viewer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, publish).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.publish:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, publish).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                        return true;
                    case R.id.view:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, viewer).commit();
                        return true;

                }

                return true;
            }
        });


    }



    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}