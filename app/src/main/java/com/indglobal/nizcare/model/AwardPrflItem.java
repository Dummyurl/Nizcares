package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/1/18.
 */

public class AwardPrflItem {

    private String id,title,college,year;

    public AwardPrflItem(String id, String title, String college, String year) {
        this.id = id;
        this.title = title;
        this.college = college;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
