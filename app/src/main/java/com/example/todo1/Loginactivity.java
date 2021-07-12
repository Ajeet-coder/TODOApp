package com.example.todo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import android.widget.Toolbar;

public class Loginactivity extends AppCompatActivity {




   //Toolbar toolbar;
    private EditText loginEmail,loginPwd;
    private Button loginBtn;
    private TextView loginQn;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_loginactivity);

//
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        mAuth=FirebaseAuth.getInstance();
        loader=new ProgressDialog(this);

        loginEmail=findViewById(R.id.loginemail);
        loginPwd=findViewById(R.id.loginpassword);
        loginBtn=findViewById(R.id.loginButton);
        loginQn=findViewById(R.id.loginpagequestion);
        //
       //toolbar=findViewById(R.id.logintoolbar);



        loginQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Loginactivity.this,RagistrationActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginEmail.getText().toString().trim();
                String password=loginPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    loginEmail.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    loginPwd.setError("Password required");
                    return;
                }else {
                    loader.setMessage("Login is in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Intent intent=new Intent(Loginactivity.this,HomeActivity.class);
                           startActivity(intent);
                           finish();
                           loader.dismiss();
                       }else {
                           String error=task.getException().toString();
                           Toast.makeText(Loginactivity.this, "Login failed"+error, Toast.LENGTH_SHORT).show();
                           loader.dismiss();

                       }
                        }
                    });
                }
            }
        });
    }
}