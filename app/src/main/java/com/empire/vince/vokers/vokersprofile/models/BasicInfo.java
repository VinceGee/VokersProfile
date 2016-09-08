package com.empire.vince.vokers.vokersprofile.models;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class BasicInfo {
    private String title, body;
    private String image;

    public BasicInfo(String imageId, String title, String body) {
        this.title = title;
        this.body = body;
        this.image = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageId() {
        return image;
    }
}
