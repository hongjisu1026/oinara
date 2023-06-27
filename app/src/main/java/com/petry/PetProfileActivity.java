package com.petry;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetProfileActivity extends AppCompatActivity {

    Button btn_profile;
    Button btn_album;
    CircleImageView pet_img;
    EditText pet_name;
    EditText pet_birth;
    RadioButton pet_male, pet_female;

    private String imgUrl = "";
    private int GALLERY_CODE = 10;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");
        mStorageRef = FirebaseStorage.getInstance().getReference();


        btn_profile = findViewById(R.id.btn_profile);
        btn_album = findViewById(R.id.btn_album);

        pet_img = findViewById(R.id.pet_img);
        pet_name = findViewById(R.id.pet_name);
        pet_birth = findViewById(R.id.pet_birth);
        pet_male = findViewById(R.id.pet_male);
        pet_female = findViewById(R.id.pet_female);


        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfile(imgUrl);
            }
        });

        //사진 선택 버튼 -> 앨범으로 이동
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent, GALLERY_CODE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE) {
            imgUrl = getRealPathFromUri(data.getData());
            Glide.with(getApplicationContext()).load(imgUrl).into(pet_img);
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
        return url;
    }

    private void uploadProfile(String uri) {
        try {
            Uri file = Uri.fromFile(new File(uri));
            final StorageReference riversRef = mStorageRef.child("images/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PetProfileActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = task.getResult();

                        PetProfile petProfile = new PetProfile();
                        petProfile.setProfileImgUrl(downloadUrl.toString());
                        petProfile.setPetName(pet_name.getText().toString());
                        petProfile.setPetBirth(pet_birth.getText().toString());
                        if (pet_male.isChecked()) {
                            petProfile.setPetSex(pet_male.getText().toString());
                        } else if (pet_female.isChecked()) {
                            petProfile.setPetSex(pet_female.getText().toString());
                        }
                        petProfile.setPetId(mAuth.getCurrentUser().getUid());

                        mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Profile").setValue(petProfile);


                        pet_img.setImageResource(R.drawable.ci_drawable);

                        Intent intent = new Intent(PetProfileActivity.this, MainActivity.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(PetProfileActivity.this, "프로필 설정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(PetProfileActivity.this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show();
        }
    }


}