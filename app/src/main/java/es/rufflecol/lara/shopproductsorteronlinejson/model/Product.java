package es.rufflecol.lara.shopproductsorteronlinejson.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}