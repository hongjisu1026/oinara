package com.petry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {

    private View view;
    private Button new_btn;
    RecyclerView album_rcv;
    private Context context;

    private List<Diary> diaryList = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @NonNull Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_album, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");
        mAuth = FirebaseAuth.getInstance();

        album_rcv = view.findViewById(R.id.album_rcv);
        new_btn = view.findViewById(R.id.new_btn);

        //글쓰기 버튼 클릭시
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewDiaryActivity.class);
                startActivity(intent);
            }
        });

        //리사이클러뷰 어댑터
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        album_rcv.setLayoutManager(layoutManager);
        final AlbumAdapter adapter = new AlbumAdapter(diaryList);
        album_rcv.setAdapter(adapter);

        //리사이클러뷰 아이템 간격
        RecyclerDecoration decoration = new RecyclerDecoration(80);
        album_rcv.addItemDecoration(decoration);

        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Diary").child(mDatabaseRef.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diaryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Diary diary = ds.getValue(Diary.class);
                    diaryList.add(diary);

                    adapter.setOnItemClickListener((albums, users) -> {
                        Intent intent = new Intent(getActivity(), DiaryDetailActivity.class);
                        intent.putExtra("ddd", diary.getDiaryId());
                        startActivity(intent);
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}