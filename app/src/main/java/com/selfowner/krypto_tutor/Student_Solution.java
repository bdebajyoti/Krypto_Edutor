package com.selfowner.krypto_tutor;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class Student_Solution extends AppCompatActivity {
    private String UID,TSID,STREAM,SEMESTER;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    ListView listView;
    private List<UploadNotes> uploadList;
    TextView backbtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__solution);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        LOADME();
        uploadList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarNotes);
        listView=findViewById(R.id.listview);
        backbtn=findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Student_Solution.this,Student_Dashboard.class));
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                UploadNotes upload = uploadList.get(i);


                //Opening the upload file in browser using the upload url
                //Intent intent = new Intent();//Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(upload.getUrl()));
                //startActivity(intent);

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.parse(upload.getUrl()),"*/*");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }
        });
    }

    private void LOADME(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_STUDENT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    TSID=dataSnapshot.child(UID).child("DETAILS").child("tsid").getValue().toString();
                    STREAM=dataSnapshot.child(UID).child("DETAILS").child("stream").getValue().toString();
                    SEMESTER=dataSnapshot.child(UID).child("DETAILS").child("semester").getValue().toString();
                    LISTVIEW(TSID,STREAM,SEMESTER);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LISTVIEW(String TSID,String STREAM,String SEMESTER){

        databaseReference= FirebaseDatabase.getInstance().getReference(""+TSID).child("SOLUTION_SECTION").child(""+SEMESTER).child(""+STREAM);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadNotes upload = postSnapshot.getValue(UploadNotes.class);

                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getFileName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Student_Solution.this,Student_Dashboard.class));
        finish();
    }
}