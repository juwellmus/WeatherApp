package com.example.musa.weatherapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class ProgressBar extends AppCompatActivity {

    RingProgressBar ringProgressBar;
    Handler handler;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        ringProgressBar = (RingProgressBar) findViewById(R.id.progressBar);
        ringProgress();
    }

    public void ringProgress()
    {
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                startActivity(new Intent(ProgressBar.this,MainActivity.class));
            }
        });

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0)
                {
                    if (progress<100){
                        progress = progress+5;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100; i++)
                {
                    try {
                        Thread.sleep(100);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
