package com.selfowner.krypto_tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Teacher_Registration extends AppCompatActivity {
    TextView backBtn;
    EditText tname,tcontact,temail,tidentity;
    Spinner qualification,department,subject,source;
    MaterialButton createaccount,profileImage;
    ImageView timage;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    String filelocation="";
    private FirebaseStorage teacherStorage;
    private StorageReference teacherRef;
    private DatabaseReference teacherDatabase;
    private FirebaseAuth firebaseAuth;
    private String TID="",TSID="",TCODE="";
    private Uri url;
    String name ;
    String contact;
    String email ;
    String qual ;
    String sub ;
    String dept ;
    String identity;
    String sourceinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__registration);
        firebaseAuth = FirebaseAuth.getInstance();
        teacherStorage = FirebaseStorage.getInstance();
        teacherRef = teacherStorage.getReference();
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Teacher_Registration.this,Selection_Page.class));
                finish();
            }
        });
        tname=findViewById(R.id.tname);
        tcontact=findViewById(R.id.tcontact);
        temail=findViewById(R.id.temail);
        tidentity=findViewById(R.id.tpanadhaar);
        qualification=findViewById(R.id.tqualification);
        department=findViewById(R.id.tdepartment);
        subject=findViewById(R.id.tsubject);
        source=findViewById(R.id.appInfo);
        timage=findViewById(R.id.timage);
        profileImage=findViewById(R.id.profileImage);

        createaccount=(MaterialButton) findViewById(R.id.createaccount);
        createaccount.setEnabled(false);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADPIC();
                createaccount.setEnabled(true);
                profileImage.setEnabled(false);
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UPLOADIMAGE();
            }
        });
    }
    private void LOADPIC(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                timage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void UPLOADIMAGE(){
        if(filePath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(Teacher_Registration.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            if(tname.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "Please Input Your FullName", Toast.LENGTH_SHORT).show();
                return;
            }
            StorageReference picstorage=teacherRef.child(tname.getText().toString()+"/"+tname.getText().toString());
            picstorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    url=uri.getResult();
                    filelocation=url.toString();
                    ADDTEACHER(filelocation);
                    progressDialog.dismiss();
                    Toast.makeText(Teacher_Registration.this, "Image Uploaded..Registration Is In Process Please Wait...!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Teacher_Registration.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading " + ((int) progress) + "% Completed");
                }
            });
        }else
        {
            Toast.makeText(this, "Please Upload Your Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void ADDTEACHER(String fileloc){
        if(filePath!=null) {
            filelocation=fileloc.toString();
            name = tname.getText().toString();
            contact = tcontact.getText().toString();
            email = temail.getText().toString();
            qual = qualification.getSelectedItem().toString();
            sub = subject.getSelectedItem().toString();
            dept = department.getSelectedItem().toString();
            identity = tidentity.getText().toString();
            sourceinfo = source.getSelectedItem().toString();
            TSID=name+"_"+contact;
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(contact)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(identity) || TextUtils.isEmpty(sourceinfo)){
                Toast.makeText(Teacher_Registration.this, "Please Fill The Form Appropriatley", Toast.LENGTH_SHORT).show();
                return;
            }
            if(qual.equalsIgnoreCase("CHOOSE QUALIFICATION TYPE")){
                Toast.makeText(Teacher_Registration.this, "Choose Qualification Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if(sub.equalsIgnoreCase("CHOOSE SUBJECT TYPE")){
                Toast.makeText(Teacher_Registration.this, "Choose Your Subject Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if(dept.equalsIgnoreCase("CHOOSE DEPARTMENT")){
                Toast.makeText(Teacher_Registration.this, "Choose Your Department", Toast.LENGTH_SHORT).show();
                return;
            }
            final ProgressDialog progressDialog = new ProgressDialog(Teacher_Registration.this);
            progressDialog.setTitle("Registering Teacher..Wait!");
            progressDialog.show();
            final Task<AuthResult> authResultTask=firebaseAuth.createUserWithEmailAndPassword(email,contact)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    TID=firebaseAuth.getCurrentUser().getUid().toString();
                                                    TCODE=TID.toUpperCase();
                                                    TCODE=TCODE.substring(0,8);
                                                    teacherDatabase= FirebaseDatabase.getInstance().getReference().child("REGISTERED_TEACHER").child(TID);
                                                    teacherDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.hasChild(""+name)){
                                                                Toast.makeText(Teacher_Registration.this,"Teacher Already Exist",Toast.LENGTH_LONG).show();
                                                                return;
                                                            }
                                                            else
                                                            {
                                                                TeacherRegHelperClass tinfo = new TeacherRegHelperClass(TID,TSID,name,contact,email,qual,dept,sub,identity,sourceinfo,filelocation,"30","","","","","","1-30",TCODE);
                                                                teacherDatabase.setValue(tinfo);
                                                                Toast.makeText(Teacher_Registration.this, "Congratulation..Data Added...!", Toast.LENGTH_LONG).show();
                                                                Toast.makeText(Teacher_Registration.this, "Verification Mail Is Sent To Your Email, Please Verify To Login", Toast.LENGTH_LONG).show();
                                                                createaccount.setEnabled(false);
                                                                tname.setText("");
                                                                tcontact.setText("");
                                                                temail.setText("");
                                                                tidentity.setText("");
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
                                Toast.makeText(Teacher_Registration.this, "Database Error", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else{
            Toast.makeText(Teacher_Registration.this, "First Upload The Profile Image", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Teacher_Registration.this,Selection_Page.class));
    }
}