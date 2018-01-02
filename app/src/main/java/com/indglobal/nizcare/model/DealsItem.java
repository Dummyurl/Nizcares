package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/2/18.
 */

public class DealsItem {

    private String master_ads_id,offer,description,media,media_thumb;

    public DealsItem(String master_ads_id, String offer, String description, String media, String media_thumb) {
        this.master_ads_id = master_ads_id;
        this.offer = offer;
        this.description = description;
        this.media = media;
        this.media_thumb = media_thumb;
    }

    public String getMaster_ads_id() {
        return master_ads_id;
    }

    public void setMaster_ads_id(String master_ads_id) {
        this.master_ads_id = master_ads_id;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMedia_thumb() {
        return media_thumb;
    }

    public void setMedia_thumb(String media_thumb) {
        this.media_thumb = media_thumb;
    }
}
