package com.selfowner.krypto_tutor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Selection_Page extends AppCompatActivity {
    RadioButton teacher,student;
    Button button,infoTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection__page);
        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.student);
        infoTag=findViewById(R.id.infoTag);
        infoTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Selection_Page.this);
                builder.setTitle("Krypto Edutor");
                builder.setIcon(R.drawable.logo);
                builder.setCancelable(false);
                builder.setMessage("Please Select Your Mode, If you are a student then select student and click on 'Get Start Now' for either login or signup. If you are Teacher then select teacher and click on 'Get Start Now' for either login or signup.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(teacher.isChecked()){
                    LOADT();
                }

                else if(student.isChecked()){
                    LOADS();
                }else{
                    Toast.makeText(Selection_Page.this, "Please Select Your Mode", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void LOADT(){
        startActivity(new Intent(Selection_Page.this, Teacher_Loading.class));
        finish();
    }
    private void LOADS(){
        startActivity(new Intent(Selection_Page.this, Student_Loading.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Selection_Page.this,Selection_Page.class));
        finish();
    }
}