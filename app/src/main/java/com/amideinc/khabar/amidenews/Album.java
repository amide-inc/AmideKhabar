package com.amideinc.khabar.amidenews;

/**
 * Created by Dell on 3/16/2018.
 */

public class Album {
    private String name;
    private String url_link;
    private int thumbnail;

    public Album() {
    }
    public Album(String name, String url_link, int thumbnail) {
        this.name = name;
        this.url_link = url_link;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
