package com.example.w3e_52.picasoimageloader;

import android.graphics.Bitmap;

/**
 * Created by w3e-52 on 3/15/16.
 */
public class Persons {



    public String id;
    public String name;
    public Bitmap bitmapImage;

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }







    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
