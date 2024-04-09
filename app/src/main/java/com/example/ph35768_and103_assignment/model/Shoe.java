package com.example.ph35768_and103_assignment.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shoe implements Serializable {
    @SerializedName("_id")
    private String id;
    private String description, favorite,id_category, name, price, avatar;

    public Shoe() {
    }

    public Shoe(String id, String color, String description, String favorite, String id_category, String name, String price, String quantity, String size, String avatar) {
        this.id = id;
        this.description = description;
        this.favorite = favorite;
        this.id_category = id_category;
        this.name = name;
        this.price = price;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
