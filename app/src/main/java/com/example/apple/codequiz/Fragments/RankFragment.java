package com.example.apple.codequiz.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.apple.codequiz.R;
import com.example.apple.codequiz.Tools.Pojo.User;
import com.example.apple.codequiz.Tools.RankViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {
    private static final String USERS_ROOT_REFERENCE = "Users";
    private static final String SCORE_REFERENCE = "score";

    FirebaseDatabase database;
    DatabaseReference usersRef;

    @BindView(R.id.rank_list)
    RecyclerView rankList;


    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rankFragmentLayout = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this,rankFragmentLayout);

        return rankFragmentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        initDatabase();
        initRecycler();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference(USERS_ROOT_REFERENCE);
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        FirebaseRecyclerAdapter<User,RankViewHolder> adapter = new FirebaseRecyclerAdapter<User, RankViewHolder>(
                User.class,
                R.layout.rank_item,
                RankViewHolder.class,
                usersRef.orderByChild(SCORE_REFERENCE)
        ) {
            @Override
            protected void populateViewHolder(RankViewHolder viewHolder, User model, int position) {
                viewHolder.rankUserName.setText(model.getName());
                viewHolder.rankUserScore.setText(String.valueOf(model.getScore() * -1));
            }
        };

        rankList.setHasFixedSize(true);
        rankList.setLayoutManager(layoutManager);
        rankList.setAdapter(adapter);

    }
}
