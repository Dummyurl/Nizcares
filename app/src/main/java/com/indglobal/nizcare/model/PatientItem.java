package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/2/18.
 */

public class PatientItem {

    private String id,name,gender,age,profile_pic,patient_type,mobile_no;

    public PatientItem(String id, String name, String gender, String age, String profile_pic, String patient_type, String mobile_no) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.profile_pic = profile_pic;
        this.patient_type = patient_type;
        this.mobile_no = mobile_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getPatient_type() {
        return patient_type;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
