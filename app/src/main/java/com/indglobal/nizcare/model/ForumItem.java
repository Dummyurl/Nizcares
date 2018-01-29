package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/24/18.
 */

public class ForumItem {

    private String forum_id,forum_name,members,join_status;

    public ForumItem(String forum_id, String forum_name, String members, String join_status) {
        this.forum_id = forum_id;
        this.forum_name = forum_name;
        this.members = members;
        this.join_status = join_status;
    }

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getForum_name() {
        return forum_name;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
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
