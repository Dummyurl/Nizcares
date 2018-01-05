package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/5/18.
 */

public class SlotItem {

    private String slot,booking_status;

    public SlotItem(String slot, String booking_status) {
        this.slot = slot;
        this.booking_status = booking_status;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }
}
