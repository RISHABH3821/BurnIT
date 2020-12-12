package com.brewingjava.burnit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.InternetService;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brewingjava.burnit.Util.API_PROVIDER.api;

public class BodyStatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_stat);
        EditText bodyWeight, bodyHeight;
        bodyWeight = findViewById(R.id.weight);
        bodyHeight = findViewById(R.id.height);
        ImageButton finishButton = findViewById(R.id.next_button);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight = Integer.parseInt(bodyWeight.getText().toString());
                int height = Integer.parseInt(bodyHeight.getText().toString());
                Call<String> call = api.updateStats(weight, height, firebaseAuth.getCurrentUser().getEmail());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            showMessage(response.body());
                            startActivity(new Intent(BodyStatActivity.this, MainActivity.class));
                            BodyStatActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        if (!new InternetService(BodyStatActivity.this).haveNetworkConnection()) {
                            showMessage("Not connected to internet.");
                        }
                    }
                });
            }
        });
    }


    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}