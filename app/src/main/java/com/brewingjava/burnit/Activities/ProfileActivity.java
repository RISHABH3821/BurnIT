package com.brewingjava.burnit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brewingjava.burnit.DataModels.HealthData;
import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.API_PROVIDER;
import com.brewingjava.burnit.Util.InternetService;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brewingjava.burnit.Util.API_PROVIDER.api;

public class ProfileActivity extends AppCompatActivity {

    EditText weightEt, heightEt;
    Button saveButton, logoutButton;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        weightEt = findViewById(R.id.weightEt);
        heightEt = findViewById(R.id.heightEt);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        name = findViewById(R.id.name);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        getHealthData(mAuth.getCurrentUser().getEmail());
        name.setText(mAuth.getCurrentUser().getDisplayName());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ProfileActivity.this, SignUp.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                ProfileActivity.this.finish();
            }
        });
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(weightEt.getText().toString().length() > 0 && Integer.parseInt(heightEt.getText().toString()) > 20)) {
                    Toast.makeText(ProfileActivity.this, "Enter valid weight > 0", Toast.LENGTH_SHORT).show();
                } else if (!(heightEt.getText().toString().length() > 0 && Integer.parseInt(heightEt.getText().toString()) > 80)) {
                    Toast.makeText(ProfileActivity.this, "Enter valid height > 80", Toast.LENGTH_SHORT).show();

                } else {
                    updateHealthData(mAuth.getCurrentUser().getEmail(),
                            Integer.parseInt(heightEt.getText().toString()), Integer.parseInt(weightEt.getText().toString()));
                }
            }
        });
    }


    private void updateHealthData(String email, int height, int weight) {
        Call<String> call = api.updateStats(weight, height, email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    ProfileActivity.this.finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (!new InternetService(ProfileActivity.this).haveNetworkConnection()) {
                    Toast.makeText(ProfileActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void getHealthData(String email) {
        Call<HealthData> call = API_PROVIDER.api.getHealthData(email);
        call.enqueue(new Callback<HealthData>() {
            @Override
            public void onResponse(Call<HealthData> call, Response<HealthData> response) {
                if (response.isSuccessful()) {
                    HealthData healthData = response.body();
                    heightEt.setText(healthData.getHeight());
                    weightEt.setText(healthData.getWeight());
                }
            }

            @Override
            public void onFailure(Call<HealthData> call, Throwable t) {

            }
        });
    }

}