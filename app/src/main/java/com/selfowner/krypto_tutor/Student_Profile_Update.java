package com.selfowner.krypto_tutor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Profile_Update extends AppCompatActivity {
    //firebase elements
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String UID;
    //frame var
    TextView stuName;
    Spinner yoursemester,yourstream,yourcourse;
    Button saveData,inst;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile__update);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        stuName=findViewById(R.id.stuName);
        yoursemester=findViewById(R.id.yoursemester);
        yourstream=findViewById(R.id.yourstream);
        yourcourse=findViewById(R.id.yourcourse);
        LOADME();
        inst=findViewById(R.id.inst);
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Student_Profile_Update.this);
                alert.setTitle("Information");
                alert.setMessage("Before Update Anything Check Your Name Is Correctly Appeared Or Not. After That Please Provide Your Information Then Update Your Educational Information Followed By Save Button");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
        saveData=findViewById(R.id.saveData);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UPDATEDATA();
            }
        });
    }

    private void UPDATEDATA() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT");
        String sem=yoursemester.getSelectedItem().toString();
        String str=yourstream.getSelectedItem().toString();
        String type=yourcourse.getSelectedItem().toString();
        if(!TextUtils.isEmpty(sem) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(type)){
            databaseReference.child(UID).child("DETAILS").child("semester").setValue(sem);
            databaseReference.child(UID).child("DETAILS").child("stream").setValue(str);
            databaseReference.child(UID).child("DETAILS").child("type").setValue(type);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Success");
            alert.setMessage("Your Data Is Saved..Press Back To Exit");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    LOADME();
                }
            });
            alert.setCancelable(false);
            alert.show();
        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning");
            alert.setMessage("Please Provide Full Information");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.setCancelable(false);
            alert.show();
        }
    }

    private void LOADME(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    String name=dataSnapshot.child(UID).child("DETAILS").child("name").getValue().toString();
                    String x=dataSnapshot.child(UID).child("DETAILS").child("semester").getValue().toString();
                    String y=dataSnapshot.child(UID).child("DETAILS").child("stream").getValue().toString();
                    String z=dataSnapshot.child(UID).child("DETAILS").child("type").getValue().toString();
                    if(!TextUtils.isEmpty(x) || !TextUtils.isEmpty(y) || !TextUtils.isEmpty(z)){
                        yourstream.setEnabled(false);
                        yourcourse.setEnabled(false);
                        yoursemester.setEnabled(false);
                    }
                    stuName.setText(""+name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Student_Profile_Update.this,Student_Dashboard.class));
        finish();
    }
}
