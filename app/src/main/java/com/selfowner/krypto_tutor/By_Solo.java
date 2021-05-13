package com.selfowner.krypto_tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class By_Solo extends AppCompatActivity {
    TextView back;
    EditText sname,semail,scontact;
    Spinner select;
    Button signup;
    //string var
    String name,email,contact,sel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by__solo);
        firebaseAuth=FirebaseAuth.getInstance();
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(By_Solo.this,Student_Registration.class));
                finish();
            }
        });
        sname=findViewById(R.id.sname);
        semail=findViewById(R.id.semail);
        scontact=findViewById(R.id.scontact);
        select=findViewById(R.id.selectOption);
        signup=findViewById(R.id.slogin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CREATEACC();
            }
        });
    }
    private void CREATEACC(){
        name=sname.getText().toString();
        email=semail.getText().toString();
        contact=scontact.getText().toString();
        sel=select.getSelectedItem().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Please Enter Contact NUmber", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sel.equalsIgnoreCase("SELECT")){
            Toast.makeText(this, "Please Select Valid Option", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog=new ProgressDialog(By_Solo.this);
        progressDialog.setMessage("Registering...Please Wait");
        progressDialog.show();
        Toast.makeText(By_Solo.this,"The Page Automatically Close After Registration",Toast.LENGTH_LONG).show();
        Toast.makeText(By_Solo.this,"Do not Press Back Button",Toast.LENGTH_LONG).show();
        firebaseAuth.createUserWithEmailAndPassword(email,contact).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT").child(""+name);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("DETAILS")){
                                Toast.makeText(By_Solo.this,"Student Already Exist",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT").child(""+name);
                                RegisterHelperClass registerHelperClass=new RegisterHelperClass(email,contact,sel,name);
                                databaseReference.child("DETAILS").setValue(registerHelperClass);
                                Intent intent=new Intent(By_Solo.this,Selection_Page.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }
                else{
                    Toast.makeText(By_Solo.this, "Server Error/Registration Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(By_Solo.this,Student_Registration.class));
        finish();
    }
}