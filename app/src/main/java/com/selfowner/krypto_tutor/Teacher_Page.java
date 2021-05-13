package com.selfowner.krypto_tutor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_Page extends AppCompatActivity {
    //TeacherSection
    EditText temail,tcontact;
    Button tlogin,tcreateaccount,instruction;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__page);
        firebaseAuth=FirebaseAuth.getInstance();
        //Teacher Section
        temail=findViewById(R.id.temail);
        tcontact=findViewById(R.id.tcontact);
        tlogin=findViewById(R.id.tlogin);
        instruction=findViewById(R.id.instruction);
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_Page.this);
                builder.setTitle("Krypto Edutor");
                builder.setIcon(R.drawable.logo);
                builder.setCancelable(false);
                builder.setMessage("Please select appropriate option, either 'Login' for existing teacher and 'Signup' for new teacher");
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
        tlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLOGIN();
            }
        });
        tcreateaccount=findViewById(R.id.tcreateaccount);
        tcreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Teacher_Page.this,Teacher_Registration.class));
                finish();
            }
        });
    }

    private void TLOGIN(){
        String email=temail.getText().toString();
        String contact=tcontact.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Input Email Address", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Please Input Contact Number", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog=new ProgressDialog(Teacher_Page.this);
        progressDialog.setMessage("Logging In Please Wait..");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,contact).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Teacher_Page.this,Teacher_Dashboard.class));
                        finish();
                    }else{
                        Toast.makeText(Teacher_Page.this, "Please Verify Your Email Id", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(Teacher_Page.this, "Invalid Information Provided", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Teacher_Page.this,Selection_Page.class));
        finish();
    }
}
