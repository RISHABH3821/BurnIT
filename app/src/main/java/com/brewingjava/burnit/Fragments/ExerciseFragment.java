package com.brewingjava.burnit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brewingjava.burnit.Activities.WorkoutActivity;
import com.brewingjava.burnit.R;

import static com.brewingjava.burnit.Constants.StringConstants.pushUps;
import static com.brewingjava.burnit.Constants.StringConstants.squats;

public class ExerciseFragment extends Fragment {

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button squatsButton = view.findViewById(R.id.squats_button);
        squatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WorkoutActivity.class);
                intent.putExtra(WorkoutActivity.EXERCISE_TYPE, squats);
                startActivity(intent);
            }
        });
        Button pushUpsButton = view.findViewById(R.id.pushup_button);
        pushUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WorkoutActivity.class);
                intent.putExtra(WorkoutActivity.EXERCISE_TYPE, pushUps);
                startActivity(intent);
            }
        });
    }
}