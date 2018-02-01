package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/1/18.
 */

public class EduPrflItem {

    private String id,degree,college,year;

    public EduPrflItem(String id, String degree, String college, String year) {
        this.id = id;
        this.degree = degree;
        this.college = college;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
