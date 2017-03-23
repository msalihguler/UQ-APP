package com.iamsalih.uq_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends Activity {

    private final String EXTRA_QUESTION_NUMBER = "question_number";
    private final String EXTRA_ANSWERS_KEY  = "answer_key";
    private ArrayList<Question> questions;
    private int questionNumber = 0;
    private TextView questionText;
    private LinearLayout textFieldHolder;
    private LinearLayout checkboxHolder;
    private RadioGroup radioButtonHolder;
    private Button submitButton;
    private EditText editText;
    private CheckBox checkbox_option_1;
    private CheckBox checkbox_option_2;
    private CheckBox checkbox_option_3;
    private CheckBox checkbox_option_4;
    private RadioButton radioButton_option_1;
    private RadioButton radioButton_option_2;
    private RadioButton radioButton_option_3;
    private RadioButton radioButton_option_4;
    private TextView resultsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main_view);

        String category = getResources().getString(R.string.empty_string);;
        if(getIntent().getExtras() != null) {
            category = getIntent().getExtras().getString(MainActivity.EXTRA_CATEGORY);
        }
        if(savedInstanceState != null){
            questionNumber = savedInstanceState.getInt(EXTRA_QUESTION_NUMBER);
            questions = savedInstanceState.getParcelableArrayList(EXTRA_ANSWERS_KEY);
        }
        questionText = (TextView)findViewById(R.id.question_text);
        resultsText = (TextView)findViewById(R.id.answers_text);
        textFieldHolder = (LinearLayout)findViewById(R.id.edittext_holder);
        checkboxHolder = (LinearLayout)findViewById(R.id.checkbox_holder);
        radioButtonHolder = (RadioGroup) findViewById(R.id.radio_holder);
        submitButton = (Button)findViewById(R.id.submit_answer);
        editText = (EditText)findViewById(R.id.answer_textfield);
        checkbox_option_1 = (CheckBox)findViewById(R.id.checkbox_option_1);
        checkbox_option_2 = (CheckBox)findViewById(R.id.checkbox_option_2);
        checkbox_option_3 = (CheckBox)findViewById(R.id.checkbox_option_3);
        checkbox_option_4 = (CheckBox)findViewById(R.id.checkbox_option_4);
        radioButton_option_1 = (RadioButton)findViewById(R.id.radio_option_1);
        radioButton_option_2 = (RadioButton)findViewById(R.id.radio_option_2);
        radioButton_option_3 = (RadioButton)findViewById(R.id.radio_option_3);
        radioButton_option_4 = (RadioButton)findViewById(R.id.radio_option_4);
        generateQuestions(category);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(EXTRA_QUESTION_NUMBER, questionNumber);
        savedInstanceState.putParcelableArrayList(EXTRA_ANSWERS_KEY,questions);
        super.onSaveInstanceState(savedInstanceState);
    }
    private void generateQuestions(String category) {
        questions = new ArrayList<>();
        if(category.equals(getString(R.string.sport_category_name))) {
            createQuiz(getResources().getStringArray(R.array.sport_questions_array),getResources().getStringArray(R.array.sport_options_array),getResources().getStringArray(R.array.sport_answers_array));
        }
        if(category.equals(getString(R.string.art_category_name))) {
            createQuiz(getResources().getStringArray(R.array.art_questions_array),getResources().getStringArray(R.array.art_options_array),getResources().getStringArray(R.array.art_answers_array));
        }
        if(category.equals(getString(R.string.history_category_name))) {
            createQuiz(getResources().getStringArray(R.array.history_questions_array),getResources().getStringArray(R.array.history_options_array),getResources().getStringArray(R.array.history_answers_array));
        } if(category.equals(getString(R.string.science_category_name))) {
            createQuiz(getResources().getStringArray(R.array.science_questions_array),getResources().getStringArray(R.array.science_options_array),getResources().getStringArray(R.array.science_answers_array));
        }
    }

    private void createQuiz(String[] questionExplanations,String[] questionOptions,String[] questionAnswers) {
        for(int i = 0; i<questionExplanations.length; i++) {
            Question question = new Question();
            question.setQuestionText(questionExplanations[i]);
            question.setOptions(Arrays.asList(questionOptions[i].split(",")));
            question.setQuestionsRightAnswers(Arrays.asList(questionAnswers[i].split(",")));
            questions.add(question);
        }
        generateQuizQuestion();
    }


    private void generateQuizQuestion() {
        if(isQuizContinues()) {
            String category = checkQuestionType();
            if (category.equals(getResources().getString(R.string.category_edittext))) {
                createEditTextQuestion();
            } else if (category.equals(getResources().getString(R.string.category_checkbox))) {
                createCheckboxQuestion();
            } else {
                createRadioButtonQuestion();
            }
        } else {
            showResults();
        }
    }

    private void showResults() {
        resetViews();
        int resultCounter = 0;
        int trueResult = 0;
        String finalResultWithText = getResources().getString(R.string.empty_string);
        resultsText.setVisibility(View.VISIBLE);
        for(Question question: questions) {
            if(question.isAnswerCorrect())
                trueResult++;
            else {
                String rightAnswers = getString(R.string.empty_string);
                for(String answer: question.getQuestionsRightAnswers()){
                    rightAnswers += answer+", ";
                }
                finalResultWithText += getString(R.string.right_answers,(resultCounter+1),rightAnswers);
            }
            resultCounter++;
        }
        resultsText.setText(finalResultWithText);
        String resultTitle = getString(R.string.result_text,trueResult);
        if(trueResult == questions.size())
            resultTitle += getString(R.string.full_success_message);
        questionText.setText(resultTitle);
        submitButton.setText(getString(R.string.start_new_quiz));
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isQuizContinues() {

        return questionNumber<questions.size();
    }

    private void createRadioButtonQuestion() {
        final Question question = questions.get(questionNumber);
        radioButtonHolder.setVisibility(View.VISIBLE);
        radioButton_option_1.setText(question.getOptions().get(0));
        radioButton_option_2.setText(question.getOptions().get(1));
        radioButton_option_3.setText(question.getOptions().get(2));
        radioButton_option_4.setText(question.getOptions().get(3));
        questionText.setText(question.getQuestionText());
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioButtonID = radioButtonHolder.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioButtonHolder.findViewById(radioButtonID);
                if(radioButton == null) {
                    Toast.makeText(QuizActivity.this, getString(R.string.not_selected_error), Toast.LENGTH_SHORT).show();
                } else {
                    if(radioButton.getText().toString().equalsIgnoreCase(questions.get(questionNumber).getQuestionsRightAnswers().get(0)))
                        question.setAnswerCorrect(true);
                    else
                        question.setAnswerCorrect(false);
                    questionNumber++;
                    resetViews();
                    generateQuizQuestion();
                }
            }
        });
    }


    private void resetViews() {

        textFieldHolder.setVisibility(View.GONE);
        checkboxHolder.setVisibility(View.GONE);
        radioButtonHolder.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        questionText.setText(getString(R.string.empty_string));
        radioButtonHolder.clearCheck();
        checkbox_option_1.setChecked(false);
        checkbox_option_2.setChecked(false);
        checkbox_option_3.setChecked(false);
        checkbox_option_4.setChecked(false);
        editText.getText().clear();
    }

    private void createCheckboxQuestion() {
        final Question question = questions.get(questionNumber);
        checkboxHolder.setVisibility(View.VISIBLE);
        checkbox_option_1.setText(question.getOptions().get(0));
        checkbox_option_2.setText(question.getOptions().get(1));
        checkbox_option_3.setText(question.getOptions().get(2));
        checkbox_option_4.setText(question.getOptions().get(3));
        questionText.setText(question.getQuestionText());
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> answers = new ArrayList<String>();
                if(checkbox_option_1.isChecked())
                    answers.add(checkbox_option_1.getText().toString());
                if(checkbox_option_2.isChecked())
                    answers.add(checkbox_option_2.getText().toString());
                if(checkbox_option_3.isChecked())
                    answers.add(checkbox_option_3.getText().toString());
                if(checkbox_option_4.isChecked())
                    answers.add(checkbox_option_4.getText().toString());
                if(questions.get(questionNumber).getQuestionsRightAnswers().containsAll(answers))
                    question.setAnswerCorrect(true);
                else
                    question.setAnswerCorrect(false);
                questionNumber++;
                resetViews();
                generateQuizQuestion();
            }
        });
    }

    private void createEditTextQuestion() {
        final Question question = questions.get(questionNumber);
        textFieldHolder.setVisibility(View.VISIBLE);
        questionText.setText(question.getQuestionText());
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().equalsIgnoreCase(questions.get(questionNumber).getQuestionsRightAnswers().get(0)))
                    question.setAnswerCorrect(true);
                else
                    question.setAnswerCorrect(false);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                questionNumber++;
                resetViews();
                generateQuizQuestion();
            }
        });

    }

    private String checkQuestionType() {
        Question question = questions.get(questionNumber);
        if(question.getOptions().size() == 1) {
            return getResources().getString(R.string.category_edittext);
        } else {
            if(question.getQuestionsRightAnswers().size() > 1) {
                return getResources().getString(R.string.category_checkbox);
            } else {
                return getResources().getString(R.string.category_radioButton);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isQuizContinues()) {
            createWarningDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void createWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning_title));
        builder.setMessage(getString(R.string.warning_message));
        builder.setPositiveButton(getString(R.string.accept_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            }).setNegativeButton(getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
