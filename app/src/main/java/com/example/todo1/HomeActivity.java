package com.example.todo1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;


private DatabaseReference reference;
private FirebaseAuth mAuth;
private FirebaseUser muser;
private String onlineuserid;
private ProgressDialog loader;
private String key="";
private String task;
private String description;
///
private ProgressDialog mloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // toolbar=findViewById(R.id.hometoolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo List ");
        ///
        mAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ///
        loader=new ProgressDialog(this);
        ///
        loader=new ProgressDialog(this);
        muser=mAuth.getCurrentUser();
        onlineuserid=muser.getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("task").child(onlineuserid);
        ///

        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }


        });



    }
    private void addTask() {
        AlertDialog.Builder myDialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);

        View myview=inflater.inflate(R.layout.input_file,null);
        myDialog.setView(myview);

       final AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);

        ///
        final EditText task=myview.findViewById(R.id.Task);
        final EditText discription=myview.findViewById(R.id.Description);
        Button save=myview.findViewById(R.id.SaveBtn);
        Button cancel=myview.findViewById(R.id.CanclBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String mtask=task.getText().toString().trim();
                String mdiscription=discription.getText().toString().trim();
                String id=reference.push().getKey();
                String date= DateFormat.getDateInstance().format(new Date());
                if (TextUtils.isEmpty(mtask)){
                    task.setError("task required");
                    return;
                }
                if (TextUtils.isEmpty(mdiscription)){
                    discription.setError("Discription required");
                    return;
                }else {
                    loader.setMessage("Adding data");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    Model model=new Model(mtask,mdiscription,id,date);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(HomeActivity.this, "inserted...", Toast.LENGTH_SHORT).show();

                                loader.dismiss();
                            }else {
                                String error=task.getException().toString();

                                Toast.makeText(HomeActivity.this, "Failed .."+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });

                }
                dialog.dismiss();

            }
        });


        dialog.show();

    }
}