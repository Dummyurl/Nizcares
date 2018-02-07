package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/7/18.
 */

public class TipItem {

    private String tip_plan_id,tip_name;

    public TipItem(String tip_plan_id, String tip_name) {
        this.tip_plan_id = tip_plan_id;
        this.tip_name = tip_name;
    }

    public String getTip_plan_id() {
        return tip_plan_id;
    }

    public void setTip_plan_id(String tip_plan_id) {
        this.tip_plan_id = tip_plan_id;
    }

    public String getTip_name() {
        return tip_name;
    }

    public void setTip_name(String tip_name) {
        this.tip_name = tip_name;
    }
}
