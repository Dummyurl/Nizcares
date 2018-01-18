package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/13/18.
 */

public class QAItem {

    private String question_id,title,description,speciality,answers_count,time_ago;
    private ArrayList<AnswerItem> answerItemArrayList;

    public QAItem(String question_id, String title, String description, String speciality,
                  String answers_count, String time_ago, ArrayList<AnswerItem> answerItemArrayList) {
        this.question_id = question_id;
        this.title = title;
        this.description = description;
        this.speciality = speciality;
        this.answers_count = answers_count;
        this.time_ago = time_ago;
        this.answerItemArrayList = answerItemArrayList;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAnswers_count() {
        return answers_count;
    }

    public void setAnswers_count(String answers_count) {
        this.answers_count = answers_count;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public ArrayList<AnswerItem> getAnswerItemArrayList() {
        return answerItemArrayList;
    }

    public void setAnswerItemArrayList(ArrayList<AnswerItem> answerItemArrayList) {
        this.answerItemArrayList = answerItemArrayList;
    }
}
