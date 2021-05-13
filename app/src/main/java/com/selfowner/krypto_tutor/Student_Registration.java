
package com.selfowner.krypto_tutor;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

public class Student_Registration extends AppCompatActivity {
        Button byorder,orderless,readme;
        TextView backbtn;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student__registration);
            byorder=findViewById(R.id.byTeacher);
            orderless=findViewById(R.id.byOwn);
            readme=findViewById(R.id.readMe);
            byorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Student_Registration.this,By_Teacher.class));
                    finish();
                }
            });
            orderless.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Student_Registration.this,By_Solo.class));
                    finish();
                }
            });
            readme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Student_Registration.this);
                    builder.setTitle("Krypto Edutor");
                    builder.setIcon(R.drawable.logo);
                    builder.setCancelable(false);
                    builder.setMessage("Guys now we start our platform for Referred Student basically, but from 1st August we provide our platform for no referred student where they can acquire services from our subject teachers.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
            });
            backbtn= findViewById(R.id.backBtn);
            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Student_Registration.this,Selection_Page.class));
                    finish();
                }
            });
        }

        @Override
        public void onBackPressed() {
            startActivity(new Intent(Student_Registration.this,Student_Page.class));
            finish();
        }
    }