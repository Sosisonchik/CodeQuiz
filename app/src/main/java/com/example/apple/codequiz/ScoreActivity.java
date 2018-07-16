
package com.example.apple.codequiz;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.apple.codequiz.Tools.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreActivity extends AppCompatActivity {
    private static final String USERS_ROOT_REFERENCE = "Users";
    private static final String SCORE_REFERENCE = "score";

    FirebaseDatabase database;
    DatabaseReference userRef;

    @BindView(R.id.back_home_btn)
    FloatingActionButton backHomeBtn;

    @BindView(R.id.score_view)
    TextView scoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

        initDatabase();
        setData();
    }

    private void initDatabase() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USERS_ROOT_REFERENCE).child(Common.NAME);

        int result = Common.userGlobalResult + Common.result;
        Common.userGlobalResult = result;

        userRef.child(SCORE_REFERENCE).setValue(result * -1);
    }

    @OnClick(R.id.back_home_btn)
    public void onBackClick(View view) {
        Intent homeActivity = new Intent(ScoreActivity.this,HomeActivity.class);
        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Common.result = 0;
        startActivity(homeActivity);
    }

    private void setData() {
        scoreView.setText(String.valueOf(Common.result));
    }
}
