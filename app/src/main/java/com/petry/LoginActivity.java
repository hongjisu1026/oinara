package com.petry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private Button register_btn;
    private Button login_btn;
    private EditText email_edt;
    private EditText pwd_edt;
    private LoginActivity activity;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");

        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);
        email_edt = findViewById(R.id.email_edt);
        pwd_edt = findViewById(R.id.pwd_edt);

        //회원가입 버튼 클릭 시
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭 시
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_edt.getText().toString();
                String pwd = pwd_edt.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { //로그인 성공
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(activity, "로그인되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(activity, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}