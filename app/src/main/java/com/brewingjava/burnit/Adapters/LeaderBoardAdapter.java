package com.brewingjava.burnit.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brewingjava.burnit.DataModels.LeaderBoardItem;
import com.brewingjava.burnit.R;

import java.util.List;
import java.util.Locale;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private List<LeaderBoardItem> dataSet;
    private Context context;

    public LeaderBoardAdapter(
            List<LeaderBoardItem> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_tile, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {
        holder.rank.setText(String.format(Locale.getDefault(),"%d", position + 3));
        holder.points.setText(dataSet.get(position).getSum());
        holder.name.setText(dataSet.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, name, points;
        ImageView userImage;
        ViewHolder(final View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.user_name);
            points = itemView.findViewById(R.id.user_points);
            userImage = itemView.findViewById(R.id.user_avatar);
        }

    }

}