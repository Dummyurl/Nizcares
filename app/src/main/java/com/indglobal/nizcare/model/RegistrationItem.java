package com.indglobal.nizcare.model;

import android.graphics.Bitmap;

/**
 * Created by readyassist on 12/27/17.
 */

public class RegistrationItem {

    private String regno,councilname,year;
    private Bitmap image;

    public RegistrationItem(String regno, String councilname, String year, Bitmap image) {
        this.regno = regno;
        this.councilname = councilname;
        this.year = year;
        this.image = image;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getCouncilname() {
        return councilname;
    }

    public void setCouncilname(String councilname) {
        this.councilname = councilname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
