package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);

                }catch (InterruptedException e){
                    e.printStackTrace();

                }finally {
                    Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                    startActivity(intent);
                    finish();
                }

            }
        };
        thread.start();

    }

}
