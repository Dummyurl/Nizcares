package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/29/18.
 */

public class ReferDocImgItem {

    private String id,media,media_thumb;

    public ReferDocImgItem(String id, String media, String media_thumb) {
        this.id = id;
        this.media = media;
        this.media_thumb = media_thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
