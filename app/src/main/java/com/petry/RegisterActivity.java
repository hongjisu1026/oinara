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


public class RegisterActivity extends AppCompatActivity {

    private RegisterActivity activity;

    private Button btn_reg;
    private EditText email_reg;
    private EditText pwd_reg;
    private EditText pwd_reg2;
    private EditText name_reg;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("petry");

        btn_reg = findViewById(R.id.btn_reg);
        email_reg = findViewById(R.id.email_reg);
        pwd_reg = findViewById(R.id.pwd_reg);
        pwd_reg2 = findViewById(R.id.pwd_reg2);
        name_reg = findViewById(R.id.name_reg);

        //버튼 클릭 시
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_reg:
                        signUp();
                        break;
                }
            }
        });

    }

    private void signUp() {
        String email = email_reg.getText().toString();
        String pwd = pwd_reg.getText().toString();
        String pwdCheck = pwd_reg2.getText().toString();
        String name = name_reg.getText().toString();

        if(email.length()>0&&pwd.length()>0&&pwdCheck.length()>0&&name.length()>0) {
            if (pwd.equals(pwdCheck)) {
                mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //회원가입 성공
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(pwd);

                            mDatabaseRef.child("userAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(activity, "회원가입에 성공했습니다\n 로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (task.getException().toString() != null) {
                                Toast.makeText(activity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(activity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "아이디, 비밀번호, 이름은 필수 입력사항입니다.", Toast.LENGTH_SHORT).show();
        }
    }

}