package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/1/18.
 */

public class MemberPrflItem {

    private String id,title,position,year;

    public MemberPrflItem(String id, String title, String position, String year) {
        this.id = id;
        this.title = title;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
