package com.brewingjava.burnit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.os.Handler;

import com.brewingjava.burnit.Constants.NetworkConstants;
import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.API;
import com.brewingjava.burnit.Util.API_PROVIDER;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        setupRetrofit();
    }

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

    private void setupRetrofit() {
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //to set timeOut time of a call.
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(2, TimeUnit.MINUTES);
        client.writeTimeout(2, TimeUnit.MINUTES);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .client(client.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API_PROVIDER.api = retrofit.create(API.class); // initializing api with retrofit
    }
}