package com.example.bajaoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import static java.lang.Thread.sleep;

public class ActivityOpener extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opener);

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    sleep(5000);
                    Intent i = new Intent(ActivityOpener.this,MainActivity.class);
                    startActivity(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


        myThread.start();
    }
}