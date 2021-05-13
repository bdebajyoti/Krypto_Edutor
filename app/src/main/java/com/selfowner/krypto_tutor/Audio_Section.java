package com.selfowner.krypto_tutor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.core.app.ActivityCompat;
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

public class Audio_Section extends AppCompatActivity {
    TextView back;
    TextView statusRecord;
    EditText audioName;
    Spinner selectSemester;
    Button MicOn,MicOff,setFile,uploadAudio;
    private String audioFilename=null,aud="",Level,dummy;
    private MediaRecorder mediaRecorder;
    private String page="AUDIO_SECTION";
    private  static final String LOG_TAG="Record_Log";
    // Requesting permission to RECORD_AUDIO
    //database reference
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    StorageReference mStorageReference;
    //Others
    private String UID;
    private String department,tsid;
    private String GetDate;
    private String filename,levelname;
    //Futher
    ArrayAdapter<String> adapter;
    ArrayList<String> namelist=new ArrayList<>();
    ListView listView;
    Button viewList;
    private String lev="";
    private int flag=0;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio__section);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        if(checkPermissionFromDevice()){
            ///
        }
        else{
            requestPermission();
        }
        back=findViewById(R.id.backBtn);
        uploadAudio=findViewById(R.id.upload);
        viewList=findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOADDATA();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Audio_Section.this,Teacher_Dashboard.class));
                finish();
            }
        });
        listView=findViewById(R.id.listview);
        DATE();
        LOADMYINFO();

        adapter=new ArrayAdapter<String>(Audio_Section.this,android.R.layout.simple_list_item_1,namelist);
        listView.setAdapter(adapter);
        statusRecord=findViewById(R.id.statusRecord);

        audioName=findViewById(R.id.audioName);
        selectSemester=findViewById(R.id.selLevel);
        MicOn=findViewById(R.id.upload1);
        MicOff=findViewById(R.id.upload2);
        aud=audioName.getText().toString();
        setFile=findViewById(R.id.setFile);
        MicOff.setEnabled(false);
        MicOn.setEnabled(false);
        uploadAudio.setEnabled(false);
        setFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SETFUNC();
            }
        });


        MicOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Audio_Section.this, ""+audioFilename, Toast.LENGTH_SHORT).show();

                try {

                    audioFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioFilename.3gp";
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecorder.setOutputFile(audioFilename);
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                MicOn.setEnabled(false);
                MicOff.setEnabled(true);
                statusRecord.setText("Recording Start...");
            }

        });
        MicOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                MicOn.setEnabled(true);
                MicOff.setEnabled(false);
                statusRecord.setText("Recording Stop...");
                Toast.makeText(Audio_Section.this, "Audio Is Saved In External Drive", Toast.LENGTH_SHORT).show();
                uploadAudio.setEnabled(true);
            }
        });
        uploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFile();
            }
        });
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(Audio_Section.this,new String[]{
                Manifest.permission.RECORD_AUDIO
        },MY_PERMISSIONS_RECORD_AUDIO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_RECORD_AUDIO:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int recordaudio=ContextCompat.checkSelfPermission(Audio_Section.this,Manifest.permission.RECORD_AUDIO);
        return recordaudio==PackageManager.PERMISSION_GRANTED;
    }


    private void UploadFile() {
        final ProgressDialog progressDialog=new ProgressDialog(Audio_Section.this);
        progressDialog.setMessage("File Is Uploading");
        progressDialog.show();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference sRef = mStorageReference.child(department + "/" + Level + "/" +  System.currentTimeMillis() + ".3gp");
        Uri uri=Uri.fromFile(new File(audioFilename));
        sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
                Uri url = uri.getResult();
                UploadNotes upload = new UploadNotes(aud, url.toString(),GetDate);
                databaseReference=FirebaseDatabase.getInstance().getReference(""+tsid);
                //Upload upload = new Upload(pdfFile.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
                //mDatabaseReference.child(""+temp).child(""+temp2).child(temp3).child("PDFNOTES").push().setValue(upload);
                databaseReference.child(""+page).child("" + Level).child("" + department).push().setValue(upload);
                audioName.setText("");
                progressDialog.dismiss();
                Toast.makeText(Audio_Section.this, "File Uploaded...!", Toast.LENGTH_SHORT).show();
                MicOn.setEnabled(false);
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

    private void SETFUNC() {
        aud=audioName.getText().toString();
        Level=selectSemester.getSelectedItem().toString();
        if(TextUtils.isEmpty(aud))
        {
            Toast.makeText(this, "Provide Audio Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Level) || Level.equals("SELECT")){
            Toast.makeText(this, "Provide CLass/Semester Information", Toast.LENGTH_SHORT).show();
            return;
        }
        MicOn.setEnabled(true);
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
                                    Toast.makeText(Audio_Section.this, "Please Select Semester", Toast.LENGTH_SHORT).show();

                                }
                                else if(flag==0){
                                    Toast.makeText(Audio_Section.this, "Please Click On Set Semester", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Audio_Section.this,Teacher_Dashboard.class));
        finish();
    }
}