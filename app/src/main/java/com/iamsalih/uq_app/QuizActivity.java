package com.iamsalih.uq_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

/**
 * Created by muhammedsalihguler on 19/03/2017.
 */

public class QuizActivity extends AppCompatActivity {

    private List<Question> questions;
    private int questionNumber;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main_view);

        String category = "";
        if(getIntent().getExtras() != null) {
            category = getIntent().getExtras().getString(MainActivity.EXTRA_CATEGORY);
            getSupportActionBar().setTitle(category);
        }
        questionText = (TextView)findViewById(R.id.question_text);
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
        createQuiz(category);
    }

    private void createQuiz(String category) {
        questions = new ArrayList<>();
        if(category.equals(getString(R.string.sport_category_name))) {
            createSportQuiz();
        }
        if(category.equals(getString(R.string.art_category_name))) {
            createArtQuiz();
        }
        if(category.equals(getString(R.string.history_category_name))) {
            createHistoryQuiz();
        }
    }

    private void createHistoryQuiz() {
        String[] questionExplanations = getResources().getStringArray(R.array.history_questions_array);
        String[] questionOptions = getResources().getStringArray(R.array.history_options_array);
        String[] questionAnswers = getResources().getStringArray(R.array.history_answers_array);

        for(int i = 0; i<questionExplanations.length; i++) {
            Question question = new Question();
            question.setQuestionText(questionExplanations[i]);
            question.setOptions(Arrays.asList(questionOptions[i].split(",")));
            question.setQuestionsRightAnswers(Arrays.asList(questionAnswers[i].split(",")));
            questions.add(question);
        }
        startQuiz();
        questionNumber = 0;
    }

    private void createArtQuiz() {
        String[] questionExplanations = getResources().getStringArray(R.array.art_questions_array);
        String[] questionOptions = getResources().getStringArray(R.array.art_options_array);
        String[] questionAnswers = getResources().getStringArray(R.array.art_answers_array);

        for(int i = 0; i<questionExplanations.length; i++) {
            Question question = new Question();
            question.setQuestionText(questionExplanations[i]);
            question.setOptions(Arrays.asList(questionOptions[i].split(",")));
            question.setQuestionsRightAnswers(Arrays.asList(questionAnswers[i].split(",")));
            questions.add(question);
        }
        startQuiz();
        questionNumber = 0;
    }

    private void createSportQuiz() {
        String[] questionExplanations = getResources().getStringArray(R.array.sport_questions_array);
        String[] questionOptions = getResources().getStringArray(R.array.sport_options_array);
        String[] questionAnswers = getResources().getStringArray(R.array.sport_answers_array);

        for(int i = 0; i<questionExplanations.length; i++) {
            Question question = new Question();
            question.setQuestionText(questionExplanations[i]);
            question.setOptions(Arrays.asList(questionOptions[i].split(",")));
            question.setQuestionsRightAnswers(Arrays.asList(questionAnswers[i].split(",")));
            questions.add(question);
        }
        startQuiz();
        questionNumber = 0;
    }

    private void startQuiz() {
        if(isQuizContinues()) {
            String category = checkQuestionType();
            if (category.equals("Edittext")) {
                createEditTextQuestion();
            } else if (category.equals("Checkbox")) {
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
                    List<String> answers = new ArrayList<String>();
                    answers.add(radioButton.getText().toString());
                    isAnswerCorrect(answers);
                    questionNumber++;
                    resetViews();
                    startQuiz();
                }
            }
        });
    }

    private void isAnswerCorrect(List<String> answers) {
        if (answers.containsAll(questions.get(questionNumber).getQuestionsRightAnswers())) {

        }
    }

    private void resetViews() {

        textFieldHolder.setVisibility(View.GONE);
        checkboxHolder.setVisibility(View.GONE);
        radioButtonHolder.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        questionText.setText("");
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
                    questionNumber++;
                    resetViews();
                    startQuiz();
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
                    questionNumber++;
                    resetViews();
                    startQuiz();
            }
        });

    }

    private String checkQuestionType() {
        Question question = questions.get(questionNumber);
        if(question.getOptions().size() == 1) {
            return "Edittext";
        } else {
            if(question.getQuestionsRightAnswers().size() > 1) {
                return "Checkbox";
            } else {
                return "Radiobutton";
            }
        }
    }

}
