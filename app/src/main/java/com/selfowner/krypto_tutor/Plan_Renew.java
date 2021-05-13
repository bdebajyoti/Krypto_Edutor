package com.selfowner.krypto_tutor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Plan_Renew extends AppCompatActivity {
    TextView studentCounter,payCategory,paymentNeed;
    TextView back;
    //firebase var
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String UID;
    private String TID;
    private String current_date;
    Button updatePlan,makePayment;
    EditText paymentId;
    private String yearlymillies,current_time;
    //Extra variable for Review and update
    //payment link:https://imjo.in/rnZqGA
    //extra variable
    private String category="";
    private String amount="";
    private String newplan="";
    private String defaultprice="";
    private String payamount="";
    private String lastpay="";
    private String oldate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan__renew);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        DATE();
        makePayment=findViewById(R.id.makePayment);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Plan_Renew.this,Make_Payment.class));
                finish();
            }
        });
        paymentId=findViewById(R.id.paymentId);
        updatePlan=findViewById(R.id.updatePlan);
        updatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REVIEWPLAN();
            }
        });
        payCategory=findViewById(R.id.payCategory);
        paymentNeed=findViewById(R.id.paymentNeed);
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Plan_Renew.this,Teacher_Dashboard.class));
                finish();
            }
        });
        studentCounter=findViewById(R.id.studentCounter);
        LOADDATA();

    }
    private void REVIEWPLAN(){
        String payId=paymentId.getText().toString();
        String genid=defaultprice+"_"+payId;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
        databaseReference.child(UID).child("date").setValue(""+current_date);
        databaseReference.child(UID).child("paymentId").setValue(""+genid);
        databaseReference.child(UID).child("paymentAmount").setValue(""+payamount);
        databaseReference.child(UID).child("planPolicy").setValue(""+newplan);
        databaseReference.child(UID).child("initialTime").setValue(""+(String.valueOf((System.currentTimeMillis())+Long.parseLong(yearlymillies))));
        Toast.makeText(this, "Plan Upgraded", Toast.LENGTH_SHORT).show();
    }


    private void LOADDATA(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    String x=dataSnapshot.child(UID).child("students").getValue().toString();
                    oldate=dataSnapshot.child(UID).child("date").getValue().toString();
                    String initial_time=dataSnapshot.child(UID).child("initialTime").getValue().toString();
                    String oldplan=dataSnapshot.child(UID).child("planPolicy").getValue().toString();
                    if(initial_time.equals("")){
                        initial_time="0";
                    }

                    PAYCATEGORY(x,initial_time,oldplan);
                    studentCounter.setText(""+x);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void PAYCATEGORY(String x, String initial_Time, String oldplan){
        int nos=Integer.parseInt(x);
        LOADCATEGORY(nos);
        if(oldplan.equals("1-30")){
            if(newplan.equals("1-30")){
                payamount="0";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
                updatePlan.setEnabled(false);
                makePayment.setEnabled(false);
            }
            if(newplan.equals("31-60")){
                payamount="500";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("61-120")){
                payamount="1000";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("121-150")){
                payamount="2000";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("Above 150")){
                payamount="2500";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
        }
        if(oldplan.equals("31-60")){
            if(newplan.equals("31-60")){
                payamount="0";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
                updatePlan.setEnabled(false);
                makePayment.setEnabled(false);
            }
            if(newplan.equals("61-120")){
                payamount="500";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("121-150")){
                payamount="1500";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("Above 150")){
                payamount="2500";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
        }
        if(oldplan.equals("61-120")){
            if(newplan.equals("61-120")){
                payamount="0";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
                updatePlan.setEnabled(false);
            }
            if(newplan.equals("121-150")){
                payamount="1000";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
            if(newplan.equals("Above 150")){
                payamount="2000";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
        }
        if(oldplan.equals("121-150")){
            if(newplan.equals("121-150")){
                payamount="0";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
                updatePlan.setEnabled(false);
                makePayment.setEnabled(false);
            }
            if(newplan.equals("Above 150")){
                payamount="1000";
                paymentNeed.setText("Plan:"+newplan+",Your Price:Rs."+payamount);
            }
        }
        if(oldplan.equals("Above 150")){
            if(newplan.equals("Above 150")){
                payamount="0";
                updatePlan.setEnabled(false);
                makePayment.setEnabled(false);
            }
        }

    }
    private void LOADCATEGORY(int nos) {
        if(nos>=1 && nos<=30){
            newplan="1-30";
            defaultprice="0";
            payCategory.setText(""+defaultprice);
        }
        else if(nos>=31 && nos<=60){
            newplan="31-60";
            defaultprice="500";
            payCategory.setText(""+defaultprice);
        }
        else if(nos>=61 && nos<=120){
            newplan="61-120";
            defaultprice="1000";
            payCategory.setText(""+defaultprice);
        }
        else if(nos>=121 && nos<=150){
            newplan="121-150";
            defaultprice="2000";
            payCategory.setText(""+defaultprice);
        }
        else if(nos>150){
            newplan="Above 150";
            defaultprice="2500";
            payCategory.setText(""+defaultprice);
        }
    }

    private void DATE(){

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        current_date =""+currentDate.format(todayDate);
        yearlymillies= String.valueOf(TimeUnit.DAYS.toMillis(365));
        current_time= String.valueOf(System.currentTimeMillis());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Plan_Renew.this,Teacher_Dashboard.class));
        finish();
    }
}