package com.kriskrause.geoquiz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuizActivity extends Activity {

    private static final String QUESTION_KEY_INDEX = "index";
    private static final String CHEATER_KEY_INDEX = "cheater";
    private static final String QUESTION_CHEAT_INDEX = "question_cheat";

    private Button _trueButton;
    private Button _falseButton;
    private Button _cheatButton;
    private ImageButton _nextButton;
    private ImageButton _prevButton;
    private TextView _questionTextView;
    private int _currentIndex = 0;
    private boolean _isCheater = false;

    private static final String TAG = "QuizActivityLog";
    private TrueFalse[] _questionBank = new TrueFalse[]{
        new TrueFalse(R.string.question_oceans, true),
        new TrueFalse(R.string.question_mideast, false),
        new TrueFalse(R.string.question_africa, false),
        new TrueFalse(R.string.question_americas, true),
        new TrueFalse(R.string.question_asia, true)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);
        Log.d(TAG, "onCreate(Bundle) called");
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle("Bodies of Water");

        _trueButton = (Button) findViewById(R.id.true_button);
        _falseButton = (Button) findViewById(R.id.false_button);
        _cheatButton = (Button) findViewById(R.id.cheat_button);
        _questionTextView = (TextView) findViewById(R.id.question_text_view);
        _nextButton = (ImageButton) findViewById(R.id.next_button);
        _prevButton = (ImageButton) findViewById(R.id.previous_button);

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentIndex = (_currentIndex + 1) % _questionBank.length;
                _isCheater = false;

                updateQuestion();
            }
        });

        _prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (_currentIndex == 0) {
                // Back to the last question.
                _currentIndex = _questionBank.length - 1;
            } else {
                _currentIndex = _currentIndex - 1;
            }

            updateQuestion();
            }
        });

        _trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        _falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        _cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cheatIntent = new Intent(QuizActivity.this, CheatActivity.class);

                boolean answerIsTrue = _questionBank[_currentIndex].isTrueQuestion();

                cheatIntent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(cheatIntent, 0);

                AlertDialog alertDialog = new AlertDialog.Builder(QuizActivity.this).create();
                alertDialog.setTitle("Correct answer is ...");
                /*if (_questionBank[_currentIndex].isTrueQuestion()){
                    alertDialog.setMessage("True");
                }else{
                    alertDialog.setMessage("False");
                }
                alertDialog.show();
                */
				_questionBank[_currentIndex].setHasCheated(true);

            }

        });

        _questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _currentIndex = (_currentIndex + 1) % _questionBank.length;
                updateQuestion();
            }
        });


        if (savedInstanceState != null) {
            _currentIndex = savedInstanceState.getInt(QUESTION_KEY_INDEX, 0);
            _isCheater = savedInstanceState.getBoolean(CHEATER_KEY_INDEX, false);

            boolean[] questionCheat = savedInstanceState.getBooleanArray(QUESTION_CHEAT_INDEX);

            for (int i = 0; i < questionCheat.length - 1; i++) {
                _questionBank[i].setHasCheated(questionCheat[i]);
            }
        }


        updateQuestion();
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(QUESTION_KEY_INDEX, _currentIndex);
        savedInstanceState.putBoolean(CHEATER_KEY_INDEX, _isCheater);

        boolean[] questionCheat = new boolean[_questionBank.length];

        for (int i = 0; i < _questionBank.length - 1; i++) {
            questionCheat[i] = _questionBank[i].hasCheated();
        }

        savedInstanceState.putBooleanArray(QUESTION_CHEAT_INDEX, questionCheat);
    }


    private void updateQuestion() {
        int question = _questionBank[_currentIndex].getQuestion();

        _questionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = _questionBank[_currentIndex].isTrueQuestion();
        boolean cheatedBefore = _questionBank[_currentIndex].hasCheated();

        int messageResId = 0;

        if ((userPressedTrue == answerIsTrue) && (_isCheater || cheatedBefore)) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        _isCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_IS_SHOWN, false);

        if (_isCheater) {
            _questionBank[_currentIndex].setHasCheated(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }

}