package com.empire.vince.vokers.vokersprofile.models;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class CounterInfo {
    private String number;
    private String text;

    public CounterInfo(String number, String text) {
        this.number = number;
        this.text = text;
    }

    public String getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
