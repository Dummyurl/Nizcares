package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/29/18.
 */

public class ReferDoctorItem {

    private String doctor_id,prefix,name,speciality,degree,total_reviews,rating,country_code,
            mobile_no,profile_pic,profile_pic_thumb,hospital_address,gender,experience,location,
            online_status,refered;
    private ArrayList<ReferDocImgItem> referDocImgItemArrayList;

    public ReferDoctorItem(String doctor_id, String prefix, String name, String speciality, String degree,
                           String total_reviews, String rating, String country_code, String mobile_no,
                           String profile_pic, String profile_pic_thumb, String hospital_address,
                           String gender, String experience, String location, String online_status,
                           String refered, ArrayList<ReferDocImgItem> referDocImgItemArrayList) {
        this.doctor_id = doctor_id;
        this.prefix = prefix;
        this.name = name;
        this.speciality = speciality;
        this.degree = degree;
        this.total_reviews = total_reviews;
        this.rating = rating;
        this.country_code = country_code;
        this.mobile_no = mobile_no;
        this.profile_pic = profile_pic;
        this.profile_pic_thumb = profile_pic_thumb;
        this.hospital_address = hospital_address;
        this.gender = gender;
        this.experience = experience;
        this.location = location;
        this.online_status = online_status;
        this.refered = refered;
        this.referDocImgItemArrayList = referDocImgItemArrayList;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(String total_reviews) {
        this.total_reviews = total_reviews;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getProfile_pic_thumb() {
        return profile_pic_thumb;
    }

    public void setProfile_pic_thumb(String profile_pic_thumb) {
        this.profile_pic_thumb = profile_pic_thumb;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getRefered() {
        return refered;
    }

    public void setRefered(String refered) {
        this.refered = refered;
    }

    public ArrayList<ReferDocImgItem> getReferDocImgItemArrayList() {
        return referDocImgItemArrayList;
    }

    public void setReferDocImgItemArrayList(ArrayList<ReferDocImgItem> referDocImgItemArrayList) {
        this.referDocImgItemArrayList = referDocImgItemArrayList;
    }
}
