package com.oi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button logo;
    private MainActivity activity;
    private GridView gridView = null;
    private GridViewAdapter adapter = null;

    BottomNavigationView bottomNavigationView;

    Fragment fragment_like;
    Fragment fragment_write;
    Fragment fragment_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        gridView = findViewById(R.id.product_list);
        adapter = new GridViewAdapter();

        adapter.addItem(new GridItem("마우스", "10000", R.drawable.product1));
        adapter.addItem(new GridItem("키보드", "20000", R.drawable.product2));
        adapter.addItem(new GridItem("모니터", "50000", R.drawable.product3));
        adapter.addItem(new GridItem("책", "5000", R.drawable.product4));
        adapter.addItem(new GridItem("향초", "30000", R.drawable.product5));

        gridView.setAdapter(adapter);

        fragment_like = new LikeFragment();
        fragment_write = new WriteFragment();
        fragment_my = new MyFragment();

        //그리드뷰 (임시)



        //바텀 네비
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.item_fragment1, fragment_like).commitAllowingStateLoss();
                    case R.id.item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.item_fragment2, fragment_write).commitAllowingStateLoss();
                    case R.id.item_fragment3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.item_fragment3, fragment_my).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });

        //로고 버튼 눌렀을때
        logo = findViewById(R.id.main_logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailActivity.class);
                startActivity(intent);
            }
        });
    }


    //그리드뷰 어댑터
    class GridViewAdapter extends BaseAdapter {
        ArrayList<GridItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(GridItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final GridItem gridItem = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, viewGroup, false);

                TextView tv_name = convertView.findViewById(R.id.tv_name);
                TextView tv_price = convertView.findViewById(R.id.tv_price);
                ImageView iv_icon = convertView.findViewById(R.id.iv_icon);

                tv_name.setText(gridItem.getProduct_name());
                tv_price.setText(gridItem.getPrice());
                iv_icon.setImageResource(gridItem.getResId());
            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            return convertView;
        }
    }
}