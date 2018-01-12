package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/12/18.
 */

public class HealthFeedItem {

    private String id,doctor_id,doctor_name,profilr_pic,title,post,speciality_name,time_ago,votes;

    public HealthFeedItem(String id, String doctor_id, String doctor_name, String profilr_pic,
                          String title, String post, String speciality_name, String time_ago, String votes) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.profilr_pic = profilr_pic;
        this.title = title;
        this.post = post;
        this.speciality_name = speciality_name;
        this.time_ago = time_ago;
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getProfilr_pic() {
        return profilr_pic;
    }

    public void setProfilr_pic(String profilr_pic) {
        this.profilr_pic = profilr_pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public void setSpeciality_name(String speciality_name) {
        this.speciality_name = speciality_name;
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
