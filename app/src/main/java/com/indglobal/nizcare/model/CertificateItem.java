package com.indglobal.nizcare.model;

import android.graphics.Bitmap;

/**
 * Created by readyassist on 12/26/17.
 */

public class CertificateItem {

    private String degree,college,year;
    private Bitmap image;

    public CertificateItem(String degree, String college, String year, Bitmap image) {
        this.degree = degree;
        this.college = college;
        this.year = year;
        this.image = image;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
