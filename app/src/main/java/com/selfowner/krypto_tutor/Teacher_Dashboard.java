package com.selfowner.krypto_tutor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_Dashboard extends AppCompatActivity {
    DrawerLayout navDrawer;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialTextView menu;
    MaterialCardView notes,papers,audio,video,solution,chatbox;
    MaterialTextView settings;
    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,countRef;
    private String UID,TCODE,TSID;
    private String quota_string1="YOUR STUDENT QUOTA IS";
    private int nos=0;
    TextView quota_data,ibutton,stat;
    TextView nosc;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__dashboard);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(Teacher_Dashboard.this, "permission denied to WRITE EXTERNAL STORAGE - requesting it", Toast.LENGTH_LONG).show();
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(Teacher_Dashboard.this, "permission denied to READ EXTERNAL STORAGE - requesting it", Toast.LENGTH_LONG).show();
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }


        stat=findViewById(R.id.stat);
        ibutton=findViewById(R.id.ibuton);
        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SHOWINFO();
            }
        });
        quota_data=findViewById(R.id.quota_data);
        nosc=findViewById(R.id.nosc);
        chatbox=findViewById(R.id.chatbox);
        chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Teacher_Dashboard.this);
                alert.setTitle("Information");
                alert.setMessage("This Section Is Updating..Please Wait Till 1st August,2020");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
        solution=findViewById(R.id.solutions);
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Teacher_Dashboard.this,Solution_Section.class));
                finish();
            }
        });
        video=findViewById(R.id.videos);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Teacher_Dashboard.this,Video_Section.class));
                finish();
            }
        });
        audio=findViewById(R.id.audios);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Teacher_Dashboard.this,Audio_Section.class));
                finish();
            }
        });
        papers=findViewById(R.id.papers);
        papers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Teacher_Dashboard.this);
                alert.setTitle("Information");
                alert.setMessage("This Section Is Updating..Please Wait Till 1st August,2020");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
        notes=findViewById(R.id.notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Teacher_Dashboard.this,Notes_Section.class));
                finish();
            }
        });
        menu=findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                navDrawer = findViewById(R.id.drawerLayout);
                // If the navigation drawer is not open then open it, if its already open then close it.
                if(!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
                else navDrawer.closeDrawer(Gravity.RIGHT);
            }
        });
        countRef=FirebaseDatabase.getInstance().getReference().child("STUCOUNTER");
        LOADMYINFO();
        drawerLayout=findViewById(R.id.drawerLayout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        navigationView=findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                switch (id)
                {
                    case R.id.profile:
                        drawerLayout.closeDrawer(Gravity.START);
                        Intent pro = new Intent(Teacher_Dashboard.this,Profile_Section.class);
                        startActivity(pro);
                        finish();
                        break;

                    case R.id.page1:
                        drawerLayout.closeDrawer(Gravity.START);
                        AlertDialog.Builder alert = new AlertDialog.Builder(Teacher_Dashboard.this);
                        alert.setTitle("Information");
                        alert.setMessage("Our Website Is Under Processing, So On Next Update You All Be Notified Regarding The Website");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                        //startActivity(new Intent(Main3Activity.this, About.class));
                        break;
                    case R.id.page2:
                        drawerLayout.closeDrawer(Gravity.START);
                        startActivity(new Intent(Teacher_Dashboard.this, Donate_Us.class));
                        finish();
                        break;
                    case R.id.page3:
                        drawerLayout.closeDrawer(Gravity.START);
                        LayoutInflater factory12 = LayoutInflater.from(Teacher_Dashboard.this);
                        final View textEntryView12 = factory12.inflate(R.layout.help_activity, null);
                        final AlertDialog.Builder alert12 = new AlertDialog.Builder(Teacher_Dashboard.this);
                        final TextView data = (TextView) textEntryView12.findViewById(R.id.webView);
                        data.setMovementMethod(new ScrollingMovementMethod());
                        alert12.setView(textEntryView12)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Toast.makeText(Teacher_Dashboard.this, "Thank You For Supporting Us!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        alert12.setCancelable(false);
                        alert12.show();
                        break;
                    case R.id.page4:
                        drawerLayout.closeDrawer(Gravity.START);
                        startActivity(new Intent(Teacher_Dashboard.this, Plan_Renew.class));
                        finish();
                        break;
                    case R.id.page5:
                        drawerLayout.closeDrawer(Gravity.START);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Use The Link Below To Install The Application And Register Yourself Using My Code:"+TCODE+"\n The Application Link\n https://bit.ly/3jg3c8s";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Teacher Referral");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    case R.id.page6:
                        drawerLayout.closeDrawer(Gravity.START);
                        Toast.makeText(Teacher_Dashboard.this, "Refer-It Will Update From Next Version (2.0)", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page7:
                        drawerLayout.closeDrawer(Gravity.START);
                        Toast.makeText(Teacher_Dashboard.this, "Redeem-It Will Update From Next Version (2.0)", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page8:
                        System.exit(0);
                        break;
                    case R.id.page9:
                        drawerLayout.closeDrawer(Gravity.START);
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Teacher_Dashboard.this, Selection_Page.class));
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOAD();
            }
        });
    }
    private void LOAD(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.settings_dialog, null);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final TextView oldvalue=textEntryView.findViewById(R.id.oldValue);
        final TextView totalvalue=textEntryView.findViewById(R.id.totalValue);
        final EditText newvalue = (EditText) textEntryView.findViewById(R.id.newValue);
        final EditText newvalue1 = (EditText) textEntryView.findViewById(R.id.newValue1);
        final Button btn1=textEntryView.findViewById(R.id.validate);
        final Button btn2=textEntryView.findViewById(R.id.updateplan);
        btn2.setEnabled(false);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(UID)){
                            String x=dataSnapshot.child(UID).child("students").getValue().toString();
                            oldvalue.setText(""+x);
                            btn2.setEnabled(true);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nv=newvalue.getText().toString();
                String ov=oldvalue.getText().toString();
                String nv1=newvalue1.getText().toString();
                if(TextUtils.isEmpty(nv)){
                    nv="0";
                }
                if(TextUtils.isEmpty(nv1)){
                    nv1="0";
                }
                int tot=Integer.parseInt(ov)+Integer.parseInt(nv)-Integer.parseInt(nv1);
                databaseReference=FirebaseDatabase.getInstance().getReference("REGISTERED_TEACHER");
                databaseReference.child(""+UID).child("students").setValue(String.valueOf(tot));
                totalvalue.setText(""+tot);
                Toast.makeText(Teacher_Dashboard.this, "Student Counter Updated", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(textEntryView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                LOADMYINFO();
                            }
                        });
        alert.setCancelable(false);
        alert.show();
    }
    private void LOADMYINFO(){

        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    String x=dataSnapshot.child(UID).child("students").getValue().toString();
                    String y=dataSnapshot.child(UID).child("paymentId").getValue().toString();
                    String z=dataSnapshot.child(UID).child("paymentAmount").getValue().toString();
                    String pp=dataSnapshot.child(UID).child("planPolicy").getValue().toString();
                    TCODE=dataSnapshot.child(UID).child("tcode").getValue().toString();
                    TSID=dataSnapshot.child(UID).child("tsid").getValue().toString();
                    nos=(int)Integer.parseInt(x);
                    quota_data.setText(quota_string1+" "+nos);
                    COUNTSTU(TSID);
                    VALIDATESTUDENTDATA(nos,y,z,pp);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void VALIDATESTUDENTDATA(int nos,String paymentId,String paymentAmount,String planPolicy) {
        if(nos>=1 && nos<=30){
            //Toast.makeText(this, "Sir, Welcome To Free Trial Platform. For More Information Please Refer Plan/Renew Option", Toast.LENGTH_LONG).show();
            stat.setText("FREE PLAN");
        }
        if(nos>=31 && nos<=60){
            if(nos<count && !paymentId.equals("") && paymentId.charAt(0)=='5' && planPolicy.equals("31-60") && !paymentAmount.equals("")){
                notes.setEnabled(true);
                papers.setEnabled(true);
                audio.setEnabled(true);
                video.setEnabled(true);
                chatbox.setEnabled(true);
                solution.setEnabled(true);
                stat.setText("PLAN AMOUNT PAID");
            }
            else{
                notes.setEnabled(false);
                papers.setEnabled(false);
                audio.setEnabled(false);
                video.setEnabled(false);
                chatbox.setEnabled(false);
                solution.setEnabled(false);
                stat.setText("UPGRADE PLAN BY PAYMENT");
            }
        }
        if(nos>=61 && nos<=120){
            if(nos<count && !paymentId.equals("") && paymentId.charAt(0)=='1' && planPolicy.equals("61-120") && !paymentAmount.equals("")){
                notes.setEnabled(true);
                papers.setEnabled(true);
                audio.setEnabled(true);
                video.setEnabled(true);
                chatbox.setEnabled(true);
                solution.setEnabled(true);
                stat.setText("PLAN AMOUNT PAID");
            }
            else{
                notes.setEnabled(false);
                papers.setEnabled(false);
                audio.setEnabled(false);
                video.setEnabled(false);
                chatbox.setEnabled(false);
                solution.setEnabled(false);
                stat.setText("UPGRADE PLAN BY PAYMENT");
            }
        }
        if(nos>=121 && nos<=150){
            if(nos<count && !paymentId.equals("") && paymentId.charAt(0)=='2' && planPolicy.equals("121-150") && !paymentAmount.equals("")){
                notes.setEnabled(true);
                papers.setEnabled(true);
                audio.setEnabled(true);
                video.setEnabled(true);
                chatbox.setEnabled(true);
                solution.setEnabled(true);
                stat.setText("PLAN AMOUNT PAID");
            }
            else{
                notes.setEnabled(false);
                papers.setEnabled(false);
                audio.setEnabled(false);
                video.setEnabled(false);
                chatbox.setEnabled(false);
                solution.setEnabled(false);
                stat.setText("UPGRADE PLAN BY PAYMENT");
            }
        }
        if(nos>150){
            if(nos<count && !paymentId.equals("") && paymentId.charAt(1)=='5' && planPolicy.equals("Above 150") && !paymentAmount.equals("")){
                notes.setEnabled(true);
                papers.setEnabled(true);
                audio.setEnabled(true);
                video.setEnabled(true);
                chatbox.setEnabled(true);
                solution.setEnabled(true);
                stat.setText("PLAN AMOUNT PAID");
            }
            else{
                notes.setEnabled(false);
                papers.setEnabled(false);
                audio.setEnabled(false);
                video.setEnabled(false);
                chatbox.setEnabled(false);
                solution.setEnabled(false);
                stat.setText("UPGRADE PLAN BY PAYMENT");
            }
        }

    }
    private void SHOWINFO(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.info_button, null);
//text_entry is an Layout XML file containing two text field to display in alert dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(textEntryView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(Teacher_Dashboard.this, "If Any Problem Persist Refer Help Menu For Contact", Toast.LENGTH_SHORT).show();
                            }
                        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void COUNTSTU(String TSID){
        count=0;
        countRef.child(TSID).child("NO_OF_STUDENT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                count=(int) snapshot.getChildrenCount();
                nosc.setText("Student Added:"+count);
                if(count>30){

                }
            }
            else{
                count=0;
                nosc.setText("Student Added:"+count);
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}