package com.selfowner.krypto_tutor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.BuildConfig;

import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    ImageView img,img2;
   // private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private String sLatestVersion,sCurrentVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        img2 = findViewById(R.id.img1);img2.animate().alpha(4000).setDuration(0);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        new GetLatestVersion().execute();
        handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                           // progressBar.setProgress(progressStatus);
                            textView.setText("LOADING "+progressStatus+"%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent dsp = new Intent(MainActivity.this,Selection_Page.class);
                startActivity(dsp);
                finish();
            }
        }).start();
    }
private class GetLatestVersion extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... strings) {
        try {
            sLatestVersion= Jsoup.connect("https://play.google.com/store/apps/deatils?"+getPackageName())
                    .timeout(30000)
                    .get()
                    .select("div.hAyfc:nth-chail(4)>"+
                            "span:nth-child(2)>div:nth-child(1)"+
                            ">span:nth-child(1)")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sLatestVersion;
    }

    @Override
    protected void onPreExecute() {
        sCurrentVersion= BuildConfig.VERSION_NAME;
        if(sLatestVersion!=null){
            float cVersion=Float.parseFloat(sCurrentVersion);
            float lVersion=Float.parseFloat(sLatestVersion);
            if(lVersion>cVersion){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Krypto Edutor");
                builder.setIcon(R.drawable.logo);
                builder.setCancelable(false);
                builder.setMessage("Please select appropriate option, either 'Login' for existing teacher and 'Signup' for new teacher");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       startActivity(new Intent(Intent.ACTION_VIEW,
                               Uri.parse("market://details?id="+getPackageName())));
                       dialog.dismiss();
                    }
                });
                builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        }
    }
}
}