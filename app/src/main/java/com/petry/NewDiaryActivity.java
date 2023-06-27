package com.petry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewDiaryActivity extends AppCompatActivity {

    NewDiaryActivity activity;

    EditText dn_title,dn_content;
    ImageView dn_img;
    Button img_load_btn, logo, save_btn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private String imgUrl = "";
    private int GALLERY_CODE = 10;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);

        activity = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        logo = findViewById(R.id.logo);
        img_load_btn = findViewById(R.id.img_load_btn);
        save_btn = findViewById(R.id.save_btn);
        dn_img = findViewById(R.id.dn_img);
        dn_title = findViewById(R.id.dn_title);
        dn_content = findViewById(R.id.dn_content);

        //로고
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

        //이미지 불러오기 버튼
        img_load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        //등록 버튼
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDiary(imgUrl);
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE) {
            imgUrl = getRealPathFromUri(data.getData());
            Glide.with(getApplicationContext()).load(imgUrl).into(dn_img);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return url;
    }

    private void uploadDiary(String uri) {
        try { //절대경로 설정
            Uri file = Uri.fromFile(new File(uri));
            final StorageReference reference = mStorageRef.child("diary/" + file.getLastPathSegment());
            UploadTask uploadTask = reference.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "업로드 성공", Toast.LENGTH_SHORT).show();

                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = task.getResult();

                        UserAccount account = new UserAccount();

                        Diary diary = new Diary();
                        diary.setdImgUrl(downloadUrl.toString());
                        diary.setdTitle(dn_title.getText().toString());
                        diary.setdContent(dn_content.getText().toString());
                        diary.setDiaryId(mAuth.getCurrentUser().getUid());
                        diary.setdImgName(file.getLastPathSegment());
                        diary.setdUploadDate(getTime());

                        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Diary").child(mDatabaseRef.getKey()).push().setValue(diary);

                        dn_img.setImageResource(R.drawable.new_diary_image);


                    } else {
                        Toast.makeText(activity, "다이어리 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(activity, "다이어리 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }



    private String getTime () {
        now = System.currentTimeMillis();
        date = new Date(now);
        return mFormat.format(date);
    }



}