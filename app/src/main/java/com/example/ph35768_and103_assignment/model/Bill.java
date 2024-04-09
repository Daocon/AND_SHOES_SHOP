package com.example.ph35768_and103_assignment.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    @SerializedName("_id")
    private String id;
    private String payment;
    private ArrayList<Cart> cart;
    private String id_user;
    private double total;

    public Bill() {
    }

    public Bill(String payment, ArrayList<Cart> cart, String id_user, double total) {
        this.payment = payment;
        this.cart = cart;
        this.id_user = id_user;
        this.total = total;
    }

    public Bill(String id, String payment, ArrayList<Cart> cart, String id_user, double total) {
        this.id = id;
        this.payment = payment;
        this.cart = cart;
        this.id_user = id_user;
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public ArrayList<Cart> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Cart> cart) {
        this.cart = cart;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
