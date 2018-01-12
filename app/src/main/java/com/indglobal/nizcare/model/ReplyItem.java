package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/12/18.
 */

public class ReplyItem {

    private String reply_id,doctor_id,message;

    public ReplyItem(String reply_id, String doctor_id, String message) {
        this.reply_id = reply_id;
        this.doctor_id = doctor_id;
        this.message = message;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
