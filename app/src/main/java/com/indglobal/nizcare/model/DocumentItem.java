package com.indglobal.nizcare.model;

import android.graphics.Bitmap;

/**
 * Created by readyassist on 12/27/17.
 */

public class DocumentItem {

    private String document_id,document_name;
    private Bitmap document_image;

    public DocumentItem(String document_id, String document_name, Bitmap document_image) {
        this.document_id = document_id;
        this.document_name = document_name;
        this.document_image = document_image;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public Bitmap getDocument_image() {
        return document_image;
    }

    public void setDocument_image(Bitmap document_image) {
        this.document_image = document_image;
    }
}
