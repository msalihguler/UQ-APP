package com.iamsalih.uq_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammedsalihguler on 19/03/2017.
 */

public class Question implements Parcelable {

    private String questionText;
    private List<String> questionsRightAnswers;
    private List<String> options;
    private String category;
    private boolean isAnswerCorrect;

    protected Question(Parcel in) {

        questionText = in.readString();
        questionsRightAnswers = in.createStringArrayList();
        options = in.createStringArrayList();
        category = in.readString();
        isAnswerCorrect = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {

            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {

            return new Question[size];
        }
    };

    public boolean isAnswerCorrect() {

        return isAnswerCorrect;
    }

    public void setAnswerCorrect(boolean answerCorrect) {

        isAnswerCorrect = answerCorrect;
    }

    public Question(){
        questionsRightAnswers = new ArrayList<>();
        options = new ArrayList<>();
    }

    public String getQuestionText() {

        return questionText;
    }

    public void setQuestionText(String questionText) {

        this.questionText = questionText;
    }

    public List<String> getQuestionsRightAnswers() {

        return questionsRightAnswers;
    }

    public void setQuestionsRightAnswers(List<String> questionsRightAnswers) {

        this.questionsRightAnswers = questionsRightAnswers;
    }

    public List<String> getOptions() {

        return options;
    }

    public void setOptions(List<String> options) {

        this.options = options;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(questionText);
        dest.writeStringList(questionsRightAnswers);
        dest.writeStringList(options);
        dest.writeString(category);
        dest.writeByte((byte) (isAnswerCorrect ? 1 : 0));
    }
}
