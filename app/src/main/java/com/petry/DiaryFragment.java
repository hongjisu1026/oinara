package com.petry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DiaryFragment extends Fragment{

    private View view;
    private Button new_btn;
    RecyclerView diary_rcv;
    private Context context;

    private List<Diary> diaryList = new ArrayList<>();

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @NonNull  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");
        mAuth = FirebaseAuth.getInstance();
        diary_rcv = view.findViewById(R.id.diary_rcv);

        //글쓰기 버튼 클릭시
        new_btn = view.findViewById(R.id.new_btn);
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewDiaryActivity.class);
                startActivity(intent);
            }
        });

        //리사이클러뷰 어댑터 설정
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        diary_rcv.setLayoutManager(layoutManager);
        final DiaryAdapter diaryAdapter = new DiaryAdapter(diaryList);
        diary_rcv.setAdapter(diaryAdapter);

        //리사이클러뷰 아이템 간격
        RecyclerDecoration decoration = new RecyclerDecoration(50);
        diary_rcv.addItemDecoration(decoration);



        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Diary").child(mDatabaseRef.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diaryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Diary diary = ds.getValue(Diary.class);
                    diaryList.add(diary);

                    diaryAdapter.setOnItemClickListener((albums, users) -> {
                        Intent intent = new Intent(getActivity(), DiaryDetailActivity.class);
                        intent.putExtra("ddd", diary.getDiaryId());
                        startActivity(intent);
                    });
                    diaryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }



}