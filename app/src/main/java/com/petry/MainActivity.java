package com.petry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity;

    BottomNavigationView bottomNavigationView;

    MainFragment mainFragment;
    DiaryFragment diaryFragment;
    AlbumFragment albumFragment;
    SettingFragment settingFragment;

    Button logout_btn;
    Button remove_btn;

    private FirebaseAuth mAuth;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        logout_btn = findViewById(R.id.logout_btn);
        remove_btn = findViewById(R.id.remove_btn);

        mainFragment = new MainFragment();
        diaryFragment = new DiaryFragment();
        albumFragment = new AlbumFragment();
        settingFragment = new SettingFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, mainFragment).commit();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, mainFragment).commit();
                        return true;
                    case R.id.menu_item2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, diaryFragment).commit();
                        return true;
                    case R.id.menu_item3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, albumFragment).commit();
                        return true;
                    case R.id.menu_item4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, settingFragment).commit();
                        return true;

                }
                return false;
            }
        });


    }




}