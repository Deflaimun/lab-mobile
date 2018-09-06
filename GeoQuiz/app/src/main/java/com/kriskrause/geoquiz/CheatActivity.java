package com.kriskrause.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.kriskrause.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_IS_SHOWN = "com.kriskrause.geoquiz.answer_shown";

    private static final String CHEATED_KEY_INDEX = "cheated";
    private static final String ANSWER_TEXT_INDEX = "answer_text";

    private boolean _answerIsTrue;
    private TextView _answerTextView;
    private Button _showAnswer;
    private boolean _didCheat;
    private TextView _apiLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cheat);

        _answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        _answerTextView = (TextView) findViewById(R.id.answerTextView);

        _apiLevelTextView = (TextView) findViewById(R.id.apiLevelTextView);

        _showAnswer = (Button) findViewById(R.id.showAnswerButton);

        _showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_answerIsTrue) {
                    _answerTextView.setText(R.string.true_button);
                } else {
                    _answerTextView.setText(R.string.false_button);
                }

                setAnswerShownResult(true);
            }
        });

        _apiLevelTextView.setText(String.format("%s %s", _apiLevelTextView.getText().toString(), Integer.toString(Build.VERSION.SDK_INT)));

        if (savedInstanceState != null) {
            _didCheat = savedInstanceState.getBoolean(CHEATED_KEY_INDEX, false);
            _answerTextView.setText(savedInstanceState.getString(ANSWER_TEXT_INDEX, ""));
        }

        setAnswerShownResult(_didCheat);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(CHEATED_KEY_INDEX, _didCheat);
        savedInstanceState.putString(ANSWER_TEXT_INDEX, _answerTextView.getText().toString());
    }

    private void setAnswerShownResult(boolean answerIsShown) {
        _didCheat = answerIsShown;

        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_SHOWN, _didCheat);
        setResult(RESULT_OK, data);
    }
}