package com.example.myflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SRIN on 6/8/2017.
 */
public class AnswerActivity extends AppCompatActivity {
    @InjectView(R.id.question)
    TextView mQuestion;
    @InjectView(R.id.answer)
    TextView mAnswer;

    public static final String KEY_QUESTION = "key_question";
    public static final String KEY_ANSWER = "key_answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String question = intent.getStringExtra(KEY_QUESTION);
            String answer = intent.getStringExtra(KEY_ANSWER);
            mQuestion.setText(question);
            mAnswer.setText(answer);
        }
    }
}
