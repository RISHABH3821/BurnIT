package com.brewingjava.burnit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.InternetService;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brewingjava.burnit.Util.API_PROVIDER.api;

public class LocationInput extends AppCompatActivity {

    TextView option1, option2;
    ImageButton nextButton;
    boolean option1Selected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        nextButton = findViewById(R.id.next_button);
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!option1Selected) {
                    option1.setBackground(getDrawable(R.drawable.ic_selector_bg));
                    option2.setBackground(null);
                    option1Selected = true;
                }
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option1Selected) {
                    option2.setBackground(getDrawable(R.drawable.ic_selector_bg));
                    option1.setBackground(null);
                    option1Selected = false;
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = option1Selected ? "BLR" : "HYD";
                Call<String> call = api.updateLocation(location, firebaseAuth.getCurrentUser().getEmail());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            showMessage(response.body());
                            startActivity(new Intent(LocationInput.this, BodyStatActivity.class));
                            LocationInput.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        if (!new InternetService(LocationInput.this).haveNetworkConnection()) {
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