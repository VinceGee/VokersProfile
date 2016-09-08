package com.empire.vince.vokers.vokersprofile.models;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class Methodology {
    private String title, body;
    private String imageId;

    public Methodology(String imageId, String title, String body) {
        this.title = title;
        this.body = body;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageId() {
        return imageId;
    }
}
