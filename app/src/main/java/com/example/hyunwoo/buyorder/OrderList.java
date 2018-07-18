package com.example.hyunwoo.buyorder;

/**
 * Created by hyunwoo on 2018-02-14.
 */

public class OrderList {
    String product;
    String origin;
    String orderkg;
    String buykg;
    String request;
    String price;

    public OrderList(String product, String origin, String orderkg, String buykg, String price, String request) {
        this.product = product;
        this.origin = origin;
        this.orderkg = orderkg;
        this.buykg = buykg;
        this.buykg = price;
        this.request = request;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrderkg() {
        return orderkg;
    }

    public void setOrderkg(String orderkg) {
        this.orderkg = orderkg;
    }

    public String getBuykg() {
        return buykg;
    }

    public void setBuykg(String buykg) {
        this.buykg = buykg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "OrderList{" +
                "product='" + product + '\'' +
                ", origin='" + origin + '\'' +
                ", orderkg='" + orderkg + '\'' +
                ", buykg='" + buykg + '\'' +
                ", price='" + price + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
