package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/25/18.
 */

public class CnsltnChatItem {

    private String messege,time,side;

    public CnsltnChatItem(String messege, String time, String side) {
        this.messege = messege;
        this.time = time;
        this.side = side;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
