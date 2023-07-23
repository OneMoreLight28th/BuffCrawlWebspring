package com.example.demo.entity;

import org.bson.types.Binary;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDocument {

    private String name;
    private Binary iconData;
    private List<Sticker> stickers;
    private LocalDateTime transactionTime;
    private String price;
    private int paintSeed;
    private String itemFloat;
    private Binary inspectionImg;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Binary getIconData() {
        return iconData;
    }

    public void setIconData(Binary iconData) {
        this.iconData = iconData;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPaintSeed() {
        return paintSeed;
    }

    public void setPaintSeed(Integer paintSeed) {
        this.paintSeed = paintSeed;
    }

    public String getItemFloat() {
        return itemFloat;
    }

    public void setItemFloat(String itemFloat) {
        this.itemFloat = itemFloat;
    }

    public Binary getInspectionImg() {
        return inspectionImg;
    }

    public void setInspectionImg(Binary inspectionImg) {
        this.inspectionImg = inspectionImg;
    }
}
