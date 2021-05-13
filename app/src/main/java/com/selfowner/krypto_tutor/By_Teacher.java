package com.selfowner.krypto_tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class By_Teacher extends AppCompatActivity {
    TextView back;
    EditText sname,semail,scontact,scode;
    Spinner sselect,steacher;
    Button signup;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    //string var
    String name,email,contact,code,select,teachername;
    //other var
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> namelist;
    //checking criteria
    String checkcode="";
    private String UID;
    private String TSID;
    //dummy var
    private String nameT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by__teacher);
        firebaseAuth=FirebaseAuth.getInstance();
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(By_Teacher.this,Student_Registration.class));
                finish();
            }
        });
        sname=findViewById(R.id.sname);
        semail=findViewById(R.id.semail);
        scontact=findViewById(R.id.scontact);
        scode=findViewById(R.id.scode);
        sselect=findViewById(R.id.selectOption);
        steacher=findViewById(R.id.selectTeacher);
        databaseReference= FirebaseDatabase.getInstance().getReference("REGISTERED_TEACHER");
        namelist=new ArrayList<>();
        adapter=new ArrayAdapter<String>(By_Teacher.this,android.R.layout.simple_spinner_dropdown_item,namelist);
        steacher.setAdapter(adapter);
        RetData();
        signup=findViewById(R.id.slogin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADTSID();


            }
        });
    }

    private void LOADTSID() {
        final String tname=steacher.getSelectedItem().toString();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    nameT=(item.child("name").getValue().toString());
                    if(nameT.equals(tname)){
                        checkcode=(item.child("tcode").getValue().toString());
                        TSID=(item.child("tsid").getValue().toString());
                        Toast.makeText(By_Teacher.this, ""+code+" "+TSID, Toast.LENGTH_SHORT).show();
                        CREATEACC();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RetData() {
        namelist.clear();
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    namelist.add(item.child("name").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void CREATEACC(){
        name=sname.getText().toString();
        email=semail.getText().toString();
        contact=scontact.getText().toString();
        code=scode.getText().toString();
        select=sselect.getSelectedItem().toString();
        teachername=steacher.getSelectedItem().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Please Enter Contact Number", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(code)){
            Toast.makeText(this, "Please Enter Code", Toast.LENGTH_LONG).show();
            return;
        }
        else if(select.equalsIgnoreCase("SELECT")){
            Toast.makeText(this, "Please Choose Valid Option", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            CHECKCODE();
        }


    }
    private void CHECKCODE(){
        if(!code.equals(checkcode))
        {
            Toast.makeText(By_Teacher.this, "Code Mismatched", Toast.LENGTH_LONG).show();
        }
        if(code.equals(checkcode)){
            Toast.makeText(By_Teacher.this, "Refer Code Matched", Toast.LENGTH_LONG).show();
            REGISTER();
        }
    }

    private void REGISTER(){
        final ProgressDialog progressDialog=new ProgressDialog(By_Teacher.this);
        progressDialog.setMessage("Registering...Please Wait");
        progressDialog.show();
        Toast.makeText(By_Teacher.this,"The Page Automatically Close After Registration",Toast.LENGTH_LONG).show();
        Toast.makeText(By_Teacher.this,"Do not Press Back Button",Toast.LENGTH_LONG).show();
        firebaseAuth.createUserWithEmailAndPassword(email,contact).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        UID=firebaseAuth.getCurrentUser().getUid().toString();
                                        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT").child(""+UID);
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild("DETAILS")){
                                                    Toast.makeText(By_Teacher.this,"Student Already Exist",Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                                else
                                                {
                                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT").child(""+UID);
                                                    RegisterHelperClassTeacher registerHelperClass=new RegisterHelperClassTeacher(name,email,contact,select,teachername,code,TSID,"","","");
                                                    databaseReference.child("DETAILS").setValue(registerHelperClass);
                                                    Toast.makeText(By_Teacher.this, "Verification Mail Sent To Your Email Id, Please Verify For Login", Toast.LENGTH_LONG).show();
                                                    databaseReference=FirebaseDatabase.getInstance().getReference().child("STUCOUNTER");
                                                    databaseReference.child(""+TSID).child("NO_OF_STUDENT").push().child("EMAIL").setValue(email);
                                                    Intent intent=new Intent(By_Teacher.this,Selection_Page.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(By_Teacher.this, "Server Error/Registration Error", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(By_Teacher.this,Student_Registration.class));
        finish();
    }
}