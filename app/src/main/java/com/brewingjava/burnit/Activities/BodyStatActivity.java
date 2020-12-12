package com.brewingjava.burnit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.brewingjava.burnit.R;

public class BodyStatActivity extends AppCompatActivity {

    private EditText bodyWeight, height;
    private ImageButton finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_stat);
        bodyWeight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        finishButton = findViewById(R.id.next_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save body stats into db
                startActivity(new Intent(BodyStatActivity.this, MainActivity.class));
                BodyStatActivity.this.finish();
            }
        });
    }
}