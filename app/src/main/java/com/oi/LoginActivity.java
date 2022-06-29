package com.oi;

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


public class LoginActivity extends AppCompatActivity {

    private Button register_btn;
    private Button login_btn;
    private EditText email_edt;
    private EditText pwd_edt;
    private LoginActivity activity;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);
        email_edt = findViewById(R.id.email_edt);
        pwd_edt = findViewById(R.id.pwd_edt);

        firebaseAuth =  FirebaseAuth.getInstance();


        //회원가입
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //로그인
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_edt.getText().toString().trim();
                String pwd = pwd_edt.getText().toString().trim();
                /*firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(activity, "로그인 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "로그인 실패, 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}