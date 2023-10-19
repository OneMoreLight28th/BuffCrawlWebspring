package com.example.main.entity;

import org.bson.types.Binary;

/**
 * 贴纸实体类
 */

public class Sticker {
    private String name;
    private int slot;
    private double wear;
    private Binary imgData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getWear() {
        return wear;
    }

    public void setWear(double wear) {
        this.wear = wear;
    }

    public Binary getImgData() {
        return imgData;
    }

    public void setImgData(Binary imgData) {
        this.imgData = imgData;
    }
}
