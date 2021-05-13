package com.selfowner.krypto_tutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Section extends AppCompatActivity {
    TextView status;
    TextView yourName,yourEmail,yourQualification,yourSpecialisation,yourDepartment,yourIdentity,yourContact,yourSource,yourStudent,yourLastPayment,yourPaymentId,yourLastPaymentDate;
    ImageView yourImage;
    private String name,email,qualification,specialisation,department,identity,contact,source,student,lastpayment,paymentid,lastpaymentdate;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String UID;
    private String imageLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__section);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        status=findViewById(R.id.status);
        yourName=findViewById(R.id.yourName);
        yourEmail=findViewById(R.id.yourEmail);
        yourQualification=findViewById(R.id.yourQualification);
        yourSpecialisation=findViewById(R.id.yourSpecialisation);
        yourDepartment=findViewById(R.id.yourDepartment);
        yourIdentity=findViewById(R.id.yourIdentity);
        yourContact=findViewById(R.id.yourContact);
        yourSource=findViewById(R.id.yourSource);
        yourStudent=findViewById(R.id.yourStudent);
        yourLastPayment=findViewById(R.id.yourLastPayment);
        yourPaymentId=findViewById(R.id.yourPaymentId);
        yourLastPaymentDate=findViewById(R.id.yourPaymentDate);
        yourImage=findViewById(R.id.yourImage);
        LOADINFO();
    }
    private void LOADINFO(){
        databaseReference= FirebaseDatabase.getInstance().getReference("REGISTERED_TEACHER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(""+UID)){
                    name=dataSnapshot.child(""+UID).child("name").getValue().toString();
                    yourName.setText(""+name);
                    email=dataSnapshot.child(""+UID).child("email").getValue().toString();
                    yourEmail.setText(""+email);
                    qualification=dataSnapshot.child(""+UID).child("qualification").getValue().toString();
                    yourQualification.setText(""+qualification);
                    specialisation=dataSnapshot.child(""+UID).child("subject").getValue().toString();
                    yourSpecialisation.setText(""+specialisation);
                    department=dataSnapshot.child(""+UID).child("department").getValue().toString();
                    yourDepartment.setText(""+department);
                    identity=dataSnapshot.child(""+UID).child("identity").getValue().toString();
                    yourIdentity.setText(""+identity);
                    contact=dataSnapshot.child(""+UID).child("contact").getValue().toString();
                    yourContact.setText(""+contact);
                    source=dataSnapshot.child(""+UID).child("sourceInfo").getValue().toString();
                    yourSource.setText(""+source);
                    student=dataSnapshot.child(""+UID).child("students").getValue().toString();
                    yourStudent.setText(""+student);
                    lastpayment=dataSnapshot.child(""+UID).child("paymentAmount").getValue().toString();

                    if(lastpayment.equals(""))
                    {
                        yourLastPayment.setText("00.00");
                    }
                    else {
                        yourLastPayment.setText("" + lastpayment);
                    }
                    paymentid=dataSnapshot.child(""+UID).child("paymentId").getValue().toString();
                    if(paymentid.equals(""))
                    {
                        yourPaymentId.setText("NULL");
                    }
                    else {
                        yourPaymentId.setText("" + paymentid);
                    }
                    lastpaymentdate=dataSnapshot.child(""+UID).child("date").getValue().toString();
                    if(lastpaymentdate.equals(""))
                    {
                        yourLastPaymentDate.setText("NULL");
                    }
                    else {
                        yourLastPaymentDate.setText("" + lastpaymentdate);
                    }
                    imageLocation=dataSnapshot.child(""+UID).child("imageFile").getValue().toString();
                    LoadImage(imageLocation);
                    status.setText("TEACHER EXIST");

                }else{
                    status.setText("TEACHER NOT EXIST");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LoadImage(String imageLocation) {
        Glide.with(this)
                .load(imageLocation)
                .into(yourImage);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile_Section.this,Teacher_Dashboard.class));
        finish();
    }
}
