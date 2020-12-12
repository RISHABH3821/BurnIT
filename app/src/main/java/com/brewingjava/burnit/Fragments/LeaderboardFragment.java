package com.brewingjava.burnit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewingjava.burnit.Adapters.LeaderBoardAdapter;
import com.brewingjava.burnit.DataModels.LeaderBoardItem;
import com.brewingjava.burnit.R;
import com.brewingjava.burnit.Util.API_PROVIDER;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    RecyclerView recyclerView;
    List<LeaderBoardItem> leaderBoardItems = new ArrayList<>();
    LeaderBoardAdapter leaderBoardAdapter;
    TextView firstRunnerUpName, secondRunnerUpName, thirdRunnerUpName, firstPoints, secondPoints, thirdPoints, userName, userPoints, userRank;
    LinearLayout bottomLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.leaderboradRv);
        leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardItems, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(leaderBoardAdapter);
        initializeUI(view);
    }


    private void initializeUI(View view) {
        firstRunnerUpName = view.findViewById(R.id.first_runnerup);
        secondRunnerUpName = view.findViewById(R.id.second_runnerup);
        thirdRunnerUpName = view.findViewById(R.id.third_runnerup);
        firstPoints = view.findViewById(R.id.first_points);
        secondPoints = view.findViewById(R.id.second_points);
        thirdPoints = view.findViewById(R.id.third_points);
        userName = view.findViewById(R.id.user_name);
        userPoints = view.findViewById(R.id.user_points);
        bottomLayout = view.findViewById(R.id.bottom_layout);
        userRank = view.findViewById(R.id.rank);
        fetchLeaderBoard();
    }


    private void fetchLeaderBoard() {
        Call<List<LeaderBoardItem>> call = API_PROVIDER.api.getLeaderBoard();
        call.enqueue(new Callback<List<LeaderBoardItem>>() {
            @Override
            public void onResponse(Call<List<LeaderBoardItem>> call, Response<List<LeaderBoardItem>> response) {
                if (response.isSuccessful()) {
                    leaderBoardItems.clear();
                    leaderBoardItems.addAll(response.body().subList(3, response.body().size()));
                    leaderBoardAdapter.notifyDataSetChanged();
                    firstRunnerUpName.setText(response.body().get(0).getName());
                    firstPoints.setText(String.format("%s pts", response.body().get(0).getSum()));
                    secondRunnerUpName.setText(response.body().get(1).getName());
                    secondPoints.setText(String.format("%s pts", response.body().get(1).getSum()));
                    thirdRunnerUpName.setText(response.body().get(2).getName());
                    thirdPoints.setText(String.format("%s pts", response.body().get(2).getSum()));
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() == null) {
                        return;
                    }
                    String currentUserName = mAuth.getCurrentUser().getDisplayName();
                    int count = 0;
                    for (LeaderBoardItem i : response.body()) {
                        count++;
                        if (i.getName().equals(currentUserName)) {
                            if (count < 3) {
                                return;
                            }
                            userName.setText(currentUserName);
                            userPoints.setText(i.getSum());
                            bottomLayout.setVisibility(View.VISIBLE);
                            userRank.setText(String.format("%d", count));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LeaderBoardItem>> call, Throwable t) {

            }
        });
    }


}