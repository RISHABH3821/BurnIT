package com.brewingjava.burnit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.brewingjava.burnit.Fragments.ExerciseFragment;
import com.brewingjava.burnit.Fragments.LeaderboardFragment;
import com.brewingjava.burnit.Fragments.StatsFragment;
import com.brewingjava.burnit.R;
import com.google.firebase.auth.FirebaseAuth;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    private static final String FRAGMENT_OTHER = "fragment_other";
    private ImageButton userProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = findViewById(R.id.bottomBar);
        userProfileButton = findViewById(R.id.user_profile_button);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignUp.class));
                MainActivity.this.finish();
            }
        });
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        StatsFragment statsFragment = new StatsFragment();
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        viewFragment(exerciseFragment, "");
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i){
                    case 0:
                        viewFragment(exerciseFragment, "");
                        break;
                    case 1:
                        viewFragment(statsFragment, FRAGMENT_OTHER);
                        break;
                    case 2:
                        viewFragment(leaderboardFragment, FRAGMENT_OTHER);
                        break;
                }
                return false;
            }
        });
    }

    private void viewFragment(Fragment fragment, String name) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        final int count = fragmentManager.getBackStackEntryCount();
        if (name.equals(FRAGMENT_OTHER)) {
            fragmentTransaction.addToBackStack(name);
        }
        fragmentTransaction.commit();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    fragmentManager.popBackStack(FRAGMENT_OTHER, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    bottomBar.bringToFront();
                }
            }
        });
    }


}