package com.example.waterplantreminder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Plant extends DataSupport{

    private int id;
    private String name;
    private double waterPerWeek;
    private double feedPerWeek;
    private int waterQuantity;
    private int feedQuantity;
    private String tips;
    private String website;
    private Date nextFeed;
    private Date nextWater;
    private byte[] image;
    private String place;

    public Plant(String name, double waterPerWeek, double feedPerWeek, int waterQuantity, int feedQuantity, String tips, String website) {
        this.name = name;
        this.waterPerWeek = waterPerWeek;
        this.feedPerWeek = feedPerWeek;
        this.waterQuantity = waterQuantity;
        this.feedQuantity = feedQuantity;
        this.tips = tips;
        this.website = website;
        this.image = null;
        this.place = null;
//        Calendar calendar = Calendar.getInstance();
//        this.nextWater = calendar.getTime();
//        this.nextWater = calendar.getTime();
    }

    public Plant() {

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the waterPerWeek
     */
    public double getWaterPerWeek() {
        return waterPerWeek;
    }

    /**
     * @return the feedPerWeek
     */
    public double getFeedPerWeek() {
        return feedPerWeek;
    }

    /**
     * @return the tips
     */
    public String getTips() {
        return tips;
    }

    public int getFeedQuantity() {
        return feedQuantity;
    }

    public int getWaterQuantity() {
        return waterQuantity;
    }

    public String getWebsite() {
        return website;
    }

    public Calendar getNextFeed() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextFeed);
        return calendar;
    }

    public Calendar getNextWater() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextWater);
        return calendar;
    }

    public byte[] getImage() {
        return image;
    }

    public Bitmap getImageBitmap(){
        if (image.length != 0) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            return null;
        }
    }

    public String getPlace() {
        return place;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param waterPerWeek the waterPerWeek to set
     */
    public void setWaterPerWeek(double waterPerWeek) {
        this.waterPerWeek = waterPerWeek;
    }

    /**
     * @param feedPerWeek the feedPerWeek to set
     */
    public void setFeedPerWeek(double feedPerWeek) {
        this.feedPerWeek = feedPerWeek;
    }

    /**
     * @param tips the tips to set
     */
    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setWaterQuantity(int waterQuantity) {
        this.waterQuantity = waterQuantity;
    }

    public void setFeedQuantity(int feedQuantity) {
        this.feedQuantity = feedQuantity;
    }

    public void setNextFeed(Calendar nextFeed) {
        this.nextFeed = nextFeed.getTime();
    }

    public void setNextWater(Calendar nextWater) {
        this.nextWater = nextWater.getTime();
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setImageBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        this.image = baos.toByteArray();
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String toString() {
        return name + "," + waterPerWeek + "," + feedPerWeek + "," + tips;
    }
}