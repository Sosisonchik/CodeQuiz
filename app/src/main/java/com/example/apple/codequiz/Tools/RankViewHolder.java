package com.example.apple.codequiz.Tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.codequiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.rank_user_name)
    public TextView rankUserName;

    @BindView(R.id.rank_user_score)
    public TextView rankUserScore;

    public RankViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
