package com.petry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DiaryDetailActivity extends AppCompatActivity {


    private Button option_btn;
    private Button logo;
    private DiaryDetailActivity activity;
    RecyclerView detail_rcv;

    private List<Diary> diaryList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>(); // key

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        activity = this;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        detail_rcv = findViewById(R.id.detail_rcv);
        logo = findViewById(R.id.logo);
        option_btn = findViewById(R.id.option_btn);

        //로고 버튼
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

        //리사이클러뷰 어댑터 설정
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        detail_rcv.setLayoutManager(layoutManager);
        final DiaryDetailAdapter adapter = new DiaryDetailAdapter(diaryList);
        detail_rcv.setAdapter(adapter);


        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Diary").child(mDatabaseRef.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diaryList.clear();
                uidList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Diary diary = ds.getValue(Diary.class);
                    String uidKey = ds.getKey();
                    diaryList.add(diary);
                    uidList.add(uidKey);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //옵션 팝업 메뉴
        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);

                getMenuInflater().inflate(R.menu.diary_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.d_menu1:
                                Toast.makeText(activity, "개발중", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.d_menu2:
                                Diary diary = new Diary();
                                removeImage(diary.getId());
                                finish();
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void removeDiary(final int position) {
        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Diary").child(mDatabaseRef.getKey()).child(uidList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeImage(final int position) {
        mStorageRef.child("diary").child(diaryList.get(position).getdImgName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                removeDiary(position);
            }
        });
    }


}