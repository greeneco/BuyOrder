package com.example.hyunwoo.buyorder;

public class Productitem {
    String product;
    String code;
    String pro1;
    String pro2;

    public Productitem() {
        this.product = product;
        this.code = code;
        this.pro1 = pro1;
        this.pro2 = pro2;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPro1() {
        return pro1;
    }

    public void setPro1(String pro1) {
        this.pro1 = pro1;
    }

    public String getPro2() {
        return pro2;
    }

    public void setPro2(String pro2) {
        this.pro2 = pro2;
    }

    @Override
    public String toString() {
        return "Productitem{" +
                "product='" + product + '\'' +
                ", code='" + code + '\'' +
                ", pro1='" + pro1 + '\'' +
                ", pro2='" + pro2 + '\'' +
                '}';
    }
}
