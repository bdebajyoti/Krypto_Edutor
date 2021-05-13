package com.selfowner.krypto_tutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Profile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String UID;
    //frame var
    TextView semail,scontact,ssub,ssemester,sstream,scourse,teachername,teachercode,yourname,stat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        semail=findViewById(R.id.sEmail);
        scontact=findViewById(R.id.sContact);
        ssub=findViewById(R.id.sSub);
        ssemester=findViewById(R.id.sSemester);
        sstream=findViewById(R.id.sStream);
        scourse=findViewById(R.id.sCourse);
        teachername=findViewById(R.id.teachername);
        teachercode=findViewById(R.id.teachercode);
        yourname=findViewById(R.id.yourName);
        stat=findViewById(R.id.status);
        LOADME();
    }
    private void LOADME(){
        databaseReference= FirebaseDatabase.getInstance().getReference("REGISTERED_STUDENT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    yourname.setText(""+dataSnapshot.child(UID).child("DETAILS").child("name").getValue().toString());
                    semail.setText(""+dataSnapshot.child(UID).child("DETAILS").child("email").getValue().toString());
                    scontact.setText(""+dataSnapshot.child(UID).child("DETAILS").child("contact").getValue().toString());
                    ssub.setText(""+dataSnapshot.child(UID).child("DETAILS").child("select").getValue().toString());
                    ssemester.setText(""+dataSnapshot.child(UID).child("DETAILS").child("semester").getValue().toString());
                    sstream.setText(""+dataSnapshot.child(UID).child("DETAILS").child("stream").getValue().toString());
                    scourse.setText(""+dataSnapshot.child(UID).child("DETAILS").child("type").getValue().toString());
                    teachername.setText(""+dataSnapshot.child(UID).child("DETAILS").child("teacherName").getValue().toString());
                    teachercode.setText(""+dataSnapshot.child(UID).child("DETAILS").child("teacherCode").getValue().toString());
                    stat.setText("STUDENT RECORD EXIST");
                }
                else{
                    stat.setText("STUDENT RECORD NOT EXIST");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Student_Profile.this,Student_Dashboard.class));
        finish();
    }
}
