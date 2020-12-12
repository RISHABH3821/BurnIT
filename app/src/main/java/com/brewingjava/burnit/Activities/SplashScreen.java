package com.brewingjava.burnit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.brewingjava.burnit.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(SplashScreen.this, SignUp.class));
                    SplashScreen.this.finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, SignUp.class));
                    SplashScreen.this.finish();
                }
            }
        }, 1500);

    }
}