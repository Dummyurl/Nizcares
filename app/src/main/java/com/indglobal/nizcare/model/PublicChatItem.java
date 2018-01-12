package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/12/18.
 */

public class PublicChatItem {

    private String group_id,group_name,members,join_status;

    public PublicChatItem(String group_id, String group_name, String members, String join_status) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.members = members;
        this.join_status = join_status;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getJoin_status() {
        return join_status;
    }

    public void setJoin_status(String join_status) {
        this.join_status = join_status;
    }
}
