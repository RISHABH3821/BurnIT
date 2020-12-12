package com.brewingjava.burnit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brewingjava.burnit.R;

public class LocationInput extends AppCompatActivity {

    TextView option1, option2;
    ImageButton nextButton;
    boolean option1Selected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        nextButton = findViewById(R.id.next_button);
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!option1Selected){
                    option1.setBackground(getDrawable(R.drawable.ic_selector_bg));
                    option2.setBackground(null);
                    option1Selected = true;
                }
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option1Selected){
                    option2.setBackground(getDrawable(R.drawable.ic_selector_bg));
                    option1.setBackground(null);
                    option1Selected = false;
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save location into db
                startActivity(new Intent(LocationInput.this, BodyStatActivity.class));
                LocationInput.this.finish();
            }
        });
    }
}