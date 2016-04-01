package es.rufflecol.lara.shopproductsorteronlinejson.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.rufflecol.lara.shopproductsorteronlinejson.model.Product;

public class ShopFeed {

    @SerializedName("currencySymbol")
    private String currencySymbol;

    @SerializedName("products")
    private List<Product> products;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}