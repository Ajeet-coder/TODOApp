package com.example.todo1;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RagistrationActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private EditText RegEmail,RegPwd;
    private Button RegBtn;
    private TextView RegQn;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ragistration);

        //
        //toolbar=findViewById(R.id.logintoolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");

        mAuth=FirebaseAuth.getInstance();
        loader=new ProgressDialog(this);

        RegEmail=findViewById(R.id.Ragistrationemail);
        RegPwd=findViewById(R.id.Ragistrationpassword);
        RegBtn=findViewById(R.id.RagistrationButton);
        RegQn=findViewById(R.id.Ragistrationpagequestion);

        RegQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RagistrationActivity.this,Loginactivity.class);
                startActivity(intent);
            }
        });

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=RegEmail.getText().toString().trim();
                String password=RegPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    RegEmail.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    RegPwd.setError("password required");
                    return;
                }else {
                    loader.setMessage("Registration is in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                Intent intent = new Intent(RagistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }else {
                                String error=task.getException().toString();
                                Toast.makeText(RagistrationActivity.this, "Registration failed"+error, Toast.LENGTH_SHORT).show();

                                loader.dismiss();
                            }
                        }
                    });
                }




            }
        });
    }
}