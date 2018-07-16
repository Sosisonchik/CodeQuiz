package com.example.apple.codequiz;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apple.codequiz.Tools.Common;
import com.example.apple.codequiz.Tools.Pojo.Category;
import com.example.apple.codequiz.Tools.Pojo.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {
    public static final String CATEGORY_NAME_TAG = "category_name";
    private static final String QUIZ_ROOT = "Quiz";

    private static final int DATABASE_MSG = 0;

    private static final String DEBUG_TAG = "startdebug";

    FirebaseDatabase database;
    DatabaseReference quizRootRef;

    @BindView(R.id.start_btn)
    FloatingActionButton startButton;

    @BindView(R.id.start_title_view)
    TextView titleView;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        titleView.setText(getCategoryName());

        initDatabaseHandler();
        initDatabase();

    }


    private void initDatabaseHandler() {
        databaseHandler = new DatabaseHandler();
    }

    private void initDatabase() {
        new Thread(() -> {
            database = FirebaseDatabase.getInstance();
            quizRootRef = database.getReference(QUIZ_ROOT);

            DatabaseReference quizRef = quizRootRef.child(getCategoryName());

            quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Common.quizes.clear();
                    for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                        Quiz quiz = quizSnapshot.getValue(Quiz.class);
                        Common.quizes.add(quiz);
                    }

                    databaseHandler.sendEmptyMessage(DATABASE_MSG);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }).start();
    }

    private String getCategoryName() {
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra(CATEGORY_NAME_TAG);

        return categoryName;
    }

    private class DatabaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATABASE_MSG){
                startButton.setOnClickListener(view -> {
                    Intent gameActivity = new Intent(StartActivity.this,GameActivity.class);
                    gameActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gameActivity);
                });
            }

        }
    }
}
