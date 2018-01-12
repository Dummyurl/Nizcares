package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class EnquiryItem {

    private String enquiry_id,patient_id,patient_name,country_code,mobile_no,problem_name,description,gender,age,time_ago;
    private ArrayList<ReplyItem> replyItemArrayList;

    public EnquiryItem(String enquiry_id, String patient_id, String patient_name, String country_code, String mobile_no, String problem_name, String description, String gender, String age, String time_ago, ArrayList<ReplyItem> replyItemArrayList) {
        this.enquiry_id = enquiry_id;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.country_code = country_code;
        this.mobile_no = mobile_no;
        this.problem_name = problem_name;
        this.description = description;
        this.gender = gender;
        this.age = age;
        this.time_ago = time_ago;
        this.replyItemArrayList = replyItemArrayList;
    }

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getProblem_name() {
        return problem_name;
    }

    public void setProblem_name(String problem_name) {
        this.problem_name = problem_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public ArrayList<ReplyItem> getReplyItemArrayList() {
        return replyItemArrayList;
    }

    public void setReplyItemArrayList(ArrayList<ReplyItem> replyItemArrayList) {
        this.replyItemArrayList = replyItemArrayList;
    }
}
