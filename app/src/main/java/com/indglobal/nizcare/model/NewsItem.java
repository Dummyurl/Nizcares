package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/2/18.
 */

public class NewsItem {

    private String master_news_id,description,media,media_thumb,logo,link;

    public NewsItem(String master_news_id, String description, String media, String media_thumb, String logo,String link) {
        this.master_news_id = master_news_id;
        this.description = description;
        this.media = media;
        this.media_thumb = media_thumb;
        this.logo = logo;
        this.link = link;
    }

    public String getMaster_news_id() {
        return master_news_id;
    }

    public void setMaster_news_id(String master_news_id) {
        this.master_news_id = master_news_id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
