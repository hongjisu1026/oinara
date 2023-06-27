package com.petry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private View view;

    private Button logout_btn;
    private Button remove_btn;
    private Button btn_profile_edit;
    private Button pwd_set;
    RecyclerView setting_rcv;
    private Context context;

    private List<PetProfile> list = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");

        logout_btn = view.findViewById(R.id.logout_btn);
        remove_btn = view.findViewById(R.id.remove_btn);
        btn_profile_edit = view.findViewById(R.id.btn_profile_edit);
        setting_rcv = view.findViewById(R.id.setting_rcv);
        pwd_set = view.findViewById(R.id.pwd_set);
        logout_btn.setOnClickListener(this);
        remove_btn.setOnClickListener(this);
        pwd_set.setOnClickListener(this);
        btn_profile_edit.setOnClickListener(this);

        btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PetProfileActivity.class);
                startActivity(intent);
            }
        });

        //프로필 이미지
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        setting_rcv.setLayoutManager(layoutManager);
        final SettingAdapter adapter = new SettingAdapter(list);
        setting_rcv.setAdapter(adapter);

        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PetProfile petProfile = ds.getValue(PetProfile.class);
                    list.add(petProfile);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pwd_set:
                updatePwd();
            case R.id.logout_btn:
                userLogout();
                break;
            case R.id.remove_btn:
                userRemove();
                break;
        }
    }


    //비밀번호 변경
    private void updatePwd() {
        Toast.makeText(getActivity(), "개발중", Toast.LENGTH_SHORT).show();
    }

    //로그아웃
    private void userLogout() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getActivity(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
        getActivity().finish();

    }

    private void userRemove() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
        alert_confirm.setMessage("회원을 탈퇴하시겠습니까?").setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mDatabaseRef.removeValue();
                                Toast.makeText(getActivity(), "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        alert_confirm.show();
    }
}