package com.example.apple.codequiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.codequiz.Tools.Common;
import com.example.apple.codequiz.Tools.Pojo.Quiz;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "gamedebug";
    Quiz quiz;
    boolean clickable;

    @BindView(R.id.query_image)
    ImageView queryImage;

    @BindView(R.id.query_title)
    TextView queryTitle;

    @BindView(R.id.next_question_btn)
    FloatingActionButton nextQuestionBtn;


    @BindView(R.id.answer_a)
    Button answerA;
    @BindView(R.id.answer_b)
    Button answerB;
    @BindView(R.id.answer_c)
    Button answerC;
    @BindView(R.id.answer_d)
    Button answerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        setData();
    }

    @SuppressLint("RestrictedApi")
    private void setData() {
        nextQuestionBtn.setVisibility(View.GONE);
        clickable = true;

        quiz = Common.quizes.get(Common.quizesIndex);
        if (quiz.getImgUrl().equals("")) {
            queryImage.setVisibility(View.GONE);
        }else {
            Picasso.get().load(quiz.getImgUrl()).into(queryImage);
            queryImage.setVisibility(View.VISIBLE);
        }

        queryTitle.setText(quiz.getQuestion());

        //setText to Buttons
        answerA.setText(quiz.getAnswA());
        answerB.setText(quiz.getAnswB());
        answerC.setText(quiz.getAnswC());
        answerD.setText(quiz.getAnswD());

    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.answer_a, R.id.answer_b, R.id.answer_c, R.id.answer_d})
    public void onAnswerClick(Button answerBtn) {
        if (clickable) {
            if (checkAnswer(answerBtn))
                queryTitle.setBackgroundColor(getResources().getColor(R.color.right_answer));
            else
                queryTitle.setBackgroundColor(getResources().getColor(R.color.wrong_answer));

            nextQuestionBtn.show();
            nextQuestionBtn.setVisibility(View.VISIBLE);
            Intent intent;

            if (Common.quizesIndex+1 <= Common.quizes.size()-1) {
                Common.quizesIndex++;
                intent = new Intent(GameActivity.this,GameActivity.class);
            } else {
                Common.quizesIndex = 0;
                intent = new Intent(GameActivity.this,ScoreActivity.class);
                Log.d(DEBUG_TAG,String.valueOf(Common.result));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            nextQuestionBtn.setOnClickListener(view -> startActivity(intent) );
        }
    }

    private boolean checkAnswer(Button answerBtn) {
        String answer = answerBtn.getText().toString();
        clickable = false;

        if (quiz.getRightAnswer().equals(answer)) {
            Common.result += 10;
            return true;
        }else{
            answerBtn.setBackgroundColor(getResources().getColor(R.color.wrong_answer));
            return false;
        }
    }



}
