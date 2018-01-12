package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/12/18.
 */

public class PrivateChatItem {

    private String question_id,patient_id,name,gender,age,title,description,time_ago;

    public PrivateChatItem(String question_id, String patient_id, String name, String gender,
                           String age, String title, String description, String time_ago) {
        this.question_id = question_id;
        this.patient_id = patient_id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.title = title;
        this.description = description;
        this.time_ago = time_ago;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }
}
