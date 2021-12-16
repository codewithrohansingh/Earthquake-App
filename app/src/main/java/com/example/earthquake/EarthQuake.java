package com.example.earthquake;

public class EarthQuake {
    private double mMagnitude;
    private String mPlace;
    private String mDate;
    private String mTime;
    private static String mLink;


    public EarthQuake(double mag, String place, String date, String time, String link) {
        this.mDate = date;
        this.mMagnitude = mag;
        this.mPlace = place;
        this.mTime = time;
        this.mLink = link;

    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public  String getLink() {
        return mLink;
    }
}
