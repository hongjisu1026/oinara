package com.oi;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    private MainActivity activity;

    BottomNavigationView bottomNavigationView;

    MainFragment mainFragment;
    ChatFragment chatFragment;
    WriteFragment writeFragment;
    LikeFragment likeFragment;
    MyFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        mainFragment = new MainFragment();
        chatFragment = new ChatFragment();
        writeFragment = new WriteFragment();
        likeFragment = new LikeFragment();
        myFragment = new MyFragment();

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, chatFragment).commit();
                        return true;
                    case R.id.menu_item3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, writeFragment).commit();
                        return true;
                    case R.id.menu_item4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, likeFragment).commit();
                        return true;
                    case R.id.menu_item5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, myFragment).commit();
                        return true;
                }
                return false;
            }
        });


    }

}