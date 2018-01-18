package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/13/18.
 */

public class AnswerItem {

    private String doctor_id,name,profilr_pic,speciality,ans_id,answers,time_ago,votes;

    public AnswerItem(String doctor_id, String name, String profilr_pic, String speciality, String ans_id, String answers, String time_ago, String votes) {
        this.doctor_id = doctor_id;
        this.name = name;
        this.profilr_pic = profilr_pic;
        this.speciality = speciality;
        this.ans_id = ans_id;
        this.answers = answers;
        this.time_ago = time_ago;
        this.votes = votes;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilr_pic() {
        return profilr_pic;
    }

    public void setProfilr_pic(String profilr_pic) {
        this.profilr_pic = profilr_pic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
