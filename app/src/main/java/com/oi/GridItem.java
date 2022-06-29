package com.oi;

public class GridItem {
    String product_name;
    String price;
    int resId;

    public GridItem(String product_name, String price, int resId) {
        this.product_name = product_name;
        this.price = price;
        this.resId = resId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name() {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice() {
        this.price = price;
    }

    public int getResId() {
        return resId;
    }

    public void setResId() {
        this.resId = resId;
    }
}
