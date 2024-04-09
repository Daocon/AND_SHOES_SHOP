package com.example.ph35768_and103_assignment.model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("_id")
    private String id;
    private String color, size, quantity, id_shoes;

    public Cart() {
    }

    public Cart(String id, String color, String size, String quantity, String id_shoes) {
        this.id = id;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.id_shoes = id_shoes;
    }

    public Cart(String color, String size, String quantity, String id_shoes) {
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.id_shoes = id_shoes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId_shoes() {
        return id_shoes;
    }

    public void setId_shoes(String id_shoes) {
        this.id_shoes = id_shoes;
    }
}
