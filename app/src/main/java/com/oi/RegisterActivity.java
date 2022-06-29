package com.oi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;



public class RegisterActivity extends AppCompatActivity {

    private EditText  email_reg, pwd_reg, pwd_reg2, name_reg, phone_reg;
    private Button btn_reg;
    private RegisterActivity activity;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;

        email_reg = findViewById(R.id.email_reg);
        pwd_reg = findViewById(R.id.pwd_reg);
        pwd_reg2 = findViewById(R.id.pwd_reg2);
        name_reg = findViewById(R.id.name_reg);
        phone_reg = findViewById(R.id.phone_reg);
        btn_reg = findViewById(R.id.btn_reg);



        //파이어베이스 권한 설정
        firebaseAuth = FirebaseAuth.getInstance();

        //회원가입 버튼 -> 파이어베이스에 데이터 저장
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //가입 정보 가져오기
                final String email = email_reg.getText().toString().trim();
                String pwd = pwd_reg.getText().toString().trim();
                String pwdcheck = pwd_reg2.getText().toString().trim();

                if (pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼" + email + ", " + pwd);
                    final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setMessage("가입중...");
                    dialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = name_reg.getText().toString().trim();
                                String phone = phone_reg.getText().toString().trim();

                                //해쉬맵 테이블을 파이어베이스 데이터에 저장
                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", email);
                                hashMap.put("name", name);
                                hashMap.put("phone", phone);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                //가입 성공시 로그인 화면으로
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "회원가입 성공, 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "회원가입 실패, 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                } else { //비밀번호 오류
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다. 확인 후 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                }

        });


    }




}