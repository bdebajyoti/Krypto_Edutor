package com.selfowner.krypto_tutor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Dashboard extends AppCompatActivity {
    TextView menu,myInfo;
    MaterialCardView notes,papers,audio,video,chatbox,solutions;
    DrawerLayout navDrawer;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String UID,TCODE;
    //loadme String var
    String semester,stream,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__dashboard);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        LOADME();
        //notes,papers,audio,video,chatbox,solutions;
        myInfo=findViewById(R.id.myInfo);
        notes=findViewById(R.id.notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Dashboard.this,Student_Note.class));
                finish();
            }
        });
        papers=findViewById(R.id.papers);
        papers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Student_Dashboard.this);
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
        audio=findViewById(R.id.audios);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Dashboard.this,Student_Audio.class));
                finish();
            }
        });
        video=findViewById(R.id.videos);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Dashboard.this,Student_Video.class));
                finish();
            }
        });
        chatbox=findViewById(R.id.chatbox);
        chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Student_Dashboard.this);
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
        solutions=findViewById(R.id.solutions);
        solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Dashboard.this,Student_Solution.class));
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
                        Intent pro = new Intent(Student_Dashboard.this,Student_Profile.class);
                        startActivity(pro);
                        finish();
                        break;
                    case R.id.profile1:
                        drawerLayout.closeDrawer(Gravity.START);
                        Intent pro1 = new Intent(Student_Dashboard.this,Student_Profile_Update.class);
                        startActivity(pro1);
                        finish();
                        break;
                    case R.id.page1:
                        drawerLayout.closeDrawer(Gravity.START);
                        AlertDialog.Builder alert = new AlertDialog.Builder(Student_Dashboard.this);
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
                        LayoutInflater factory12 = LayoutInflater.from(Student_Dashboard.this);
                        final View textEntryView12 = factory12.inflate(R.layout.help_activity, null);
                        final AlertDialog.Builder alert12 = new AlertDialog.Builder(Student_Dashboard.this);
                        final TextView data = (TextView) textEntryView12.findViewById(R.id.webView);
                        data.setMovementMethod(new ScrollingMovementMethod());
                        alert12.setView(textEntryView12)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Toast.makeText(Student_Dashboard.this, "Thank You For Supporting Us!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        alert12.setCancelable(false);
                        alert12.show();
                        break;
                    case R.id.page3:
                        drawerLayout.closeDrawer(Gravity.START);
                        Toast.makeText(Student_Dashboard.this, "Share-This First Version So Wait For 2nd Version", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page4:
                        drawerLayout.closeDrawer(Gravity.START);
                        Toast.makeText(Student_Dashboard.this, "Refer-This First Version So Wait For 2nd Version", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page5:
                        drawerLayout.closeDrawer(Gravity.START);
                        Toast.makeText(Student_Dashboard.this, "Redeem-This First Version So Wait For 2nd Version", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page6:
                        drawerLayout.closeDrawer(Gravity.START);
                        System.exit(0);
                        break;
                    case R.id.page7:
                        drawerLayout.closeDrawer(Gravity.START);
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Student_Dashboard.this, Selection_Page.class));
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }
    private void LOADME(){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    semester=dataSnapshot.child(UID).child("DETAILS").child("semester").getValue().toString();
                    stream=dataSnapshot.child(UID).child("DETAILS").child("stream").getValue().toString();
                    type=dataSnapshot.child(UID).child("DETAILS").child("type").getValue().toString();

                    if(TextUtils.isEmpty(semester) || TextUtils.isEmpty(stream) || TextUtils.isEmpty(type)){
                        //notes,papers,audio,video,chatbox,solutions;
                        //Toast.makeText(Student_Dashboard.this, "NULL", Toast.LENGTH_SHORT).show();
                        myInfo.setText("UPDATE YOUR PROFILE FROM UPDATE PROFILE");
                        notes.setEnabled(false);
                        papers.setEnabled(false);
                        audio.setEnabled(false);
                        video.setEnabled(false);
                        chatbox.setEnabled(false);
                        solutions.setEnabled(false);
                    }
                    else{
                        myInfo.setText("PROFILE IS ACTIVE");
                        notes.setEnabled(true);
                        papers.setEnabled(true);
                        audio.setEnabled(true);
                        video.setEnabled(true);
                        chatbox.setEnabled(true);
                        solutions.setEnabled(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}