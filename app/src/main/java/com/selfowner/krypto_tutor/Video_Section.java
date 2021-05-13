package com.selfowner.krypto_tutor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Video_Section extends AppCompatActivity {
    final static int PICK_PDF_CODE = 2342;
    final static int VIDEO_REQUEST_CODE = 1001;
    TextView back;
    EditText videoName;
    Spinner selLevel;
    Button setVideo,recordVideo,openVideo,uploadVideo,viewList;
    //required string var
    private String VideoFile,LevelSec;
    //firebase var
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    StorageReference mStorageReference;
    private String UID,tsid;
    private String department;
    private String GetDate;
    private String page="VIDEO_SECTION";
    private String video_file=null;
    //NEW VAR
    private ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> namelist=new ArrayList<>();
    private String lev="";
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__section);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(Video_Section.this, "permission denied to CAMERA - requesting it", Toast.LENGTH_LONG).show();
                String[] permissions = {Manifest.permission.CAMERA};
                requestPermissions(permissions, 1);
            }
        }
        DATE();
        LOADMYINFO();

        back=findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Video_Section.this,Teacher_Dashboard.class));
                finish();
            }
        });
        listView=findViewById(R.id.listview);
        adapter=new ArrayAdapter<String>(Video_Section.this,android.R.layout.simple_list_item_1,namelist);
        listView.setAdapter(adapter);
        videoName=findViewById(R.id.videoName);
        selLevel=findViewById(R.id.selLevel);
        setVideo=findViewById(R.id.setVideo);
        recordVideo=findViewById(R.id.recordVideo);
        openVideo=findViewById(R.id.openVideo);
        viewList=findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOADDATA();
            }
        });
        recordVideo.setEnabled(false);
        openVideo.setEnabled(false);
        setVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SETVIDEOFILE();
            }
        });
        openVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordVideo.setEnabled(false);
                SEARCHFILE();
            }
        });

        recordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CAPTUREVIEW();
                openVideo.setEnabled(false);
            }
        });
    }

    private void CAPTUREVIEW() {
        Toast.makeText(this, "It Is Our first Version..Wait For Updated Version", Toast.LENGTH_LONG).show();
        recordVideo.setEnabled(false);
        openVideo.setEnabled(true);
    }


    private void SEARCHFILE(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video File"), PICK_PDF_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFile(Uri data) {
        if(!TextUtils.isEmpty(VideoFile) && !TextUtils.isEmpty(LevelSec)) {
            final ProgressDialog progressDialog=new ProgressDialog(Video_Section.this);
            progressDialog.setMessage("File Is Uploading");
            progressDialog.show();
            mStorageReference = FirebaseStorage.getInstance().getReference();
            StorageReference sRef = mStorageReference.child(department + "/" + LevelSec + "/" +  System.currentTimeMillis() + ".mp4");
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            UploadNotes upload = new UploadNotes(VideoFile, url.toString(),GetDate);

                            databaseReference=FirebaseDatabase.getInstance().getReference(""+tsid);
                            //Upload upload = new Upload(pdfFile.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
                            //mDatabaseReference.child(""+temp).child(""+temp2).child(temp3).child("PDFNOTES").push().setValue(upload);
                            databaseReference.child(""+page).child("" + LevelSec).child("" + department).push().setValue(upload);
                            videoName.setText("");
                            progressDialog.dismiss();
                            Toast.makeText(Video_Section.this, "Video Uploaded...!", Toast.LENGTH_SHORT).show();
                            openVideo.setEnabled(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "% Uploading...");
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Provide The Valid Information", Toast.LENGTH_LONG).show();
        }
    }
    private void SETVIDEOFILE(){
        VideoFile=videoName.getText().toString();
        LevelSec=selLevel.getSelectedItem().toString();
        if(TextUtils.isEmpty(VideoFile)){
            Toast.makeText(this, "Please Provide Video FileName", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(LevelSec) || LevelSec.equals("SELECT")){
            Toast.makeText(this, "Please Select Class/Semester", Toast.LENGTH_SHORT).show();
            return;
        }
        openVideo.setEnabled(true);
        recordVideo.setEnabled(true);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Video_Section.this,Teacher_Dashboard.class));
        finish();
    }
    private void LOADMYINFO(){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    department=dataSnapshot.child(UID).child("department").getValue().toString();
                    tsid=dataSnapshot.child(UID).child("tsid").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void DATE(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        GetDate =""+currentDate.format(todayDate);
    }
    private void LOADDATA(){

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.view_list_layout, null);
//text_entry is an Layout XML file containing two text field to display in alert dialog
        final Spinner input1 = (Spinner) textEntryView.findViewById(R.id.selLevel);
        final Button btn=textEntryView.findViewById(R.id.loadData);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lev=input1.getSelectedItem().toString();
                btn.setBackgroundColor(Color.BLUE);
                flag=1;
            }
        });
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(textEntryView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(lev.equals("SELECT") || lev.equals("")){
                                    Toast.makeText(Video_Section.this, "Please Select Semester", Toast.LENGTH_SHORT).show();

                                }
                                else if(flag==0){
                                    Toast.makeText(Video_Section.this, "Please Click On Set Semester", Toast.LENGTH_SHORT).show();
                                    alert.setCancelable(false);
                                }
                                else{
                                    LISTVIEW(lev);
                                    alert.setCancelable(true);
                                }

                            }
                        });
        alert.setCancelable(false);
        alert.show();
    }

    private void LISTVIEW(String Level){
        namelist.clear();
        Toast.makeText(this, ""+Level, Toast.LENGTH_SHORT).show();
        databaseReference= FirebaseDatabase.getInstance().getReference(""+tsid).child(""+page).child(""+Level).child(""+department);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String xyz="Uploaded On:"+dataSnapshot.child("date").getValue().toString()+"\nFileName:"+dataSnapshot.child("fileName").getValue().toString();
                namelist.add(xyz);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
