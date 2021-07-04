package com.example.kltn.hospitalappointy.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kltn.hospitalappointy.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView mSplashText;
    private ImageView mSplashLogo;
    private Animation uptodown;
    private Animation downtoup;
//    private TextView d,n,t,a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mSplashText = (ImageView) findViewById(R.id.splash_logo1);
        mSplashLogo = (ImageView) findViewById(R.id.splash_logo);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);


        mSplashText.setAnimation(downtoup);
        mSplashLogo.setAnimation(uptodown);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    isOnline();
                }
            }
        };
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            Intent main_Intent= new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(main_Intent);
            return true;
        }
        Toast.makeText(getBaseContext(),"Connect to your wifi or mobile network to use",Toast.LENGTH_LONG).show();
        return false;

    }
}
