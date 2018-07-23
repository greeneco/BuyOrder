package com.example.hyunwoo.buyorder;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by hyunwoo on 2018-02-15.
 */
public class HttpConnection {
    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();

    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    // 발주서내역
    public void requestWebServer(String parameter, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("date", parameter)
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/Buyorderlist.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // Market 정보
    public void requestWebServer2(Callback callback2) {
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/BuyorderMarket.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback2);
    }

    public void requestWebServer3(String parameter, Double parameter2, int parameter3, String parameter4, String parameter5, String parameter6, int parameter7, Callback callback3) {
        RequestBody body = new FormBody.Builder()
                .add("company", parameter)
                .add("kg", String.valueOf(parameter2))
                .add("price", String.valueOf(parameter3))
                .add("origin", parameter4)
                .add("order", parameter5)
                .add("date", parameter6)
                .add("check", String.valueOf(parameter7))
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/Buyorderadd.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback3);
    }

    public void requestWebServer4(String parameter, Callback callback4) {
        RequestBody body = new FormBody.Builder()
                .add("product", parameter)
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/Buyorderbuylist.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback4);
    }

    public void requestWebServer5(Callback callback5) {
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/BuyorderProduct.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback5);
    }

    public void requestWebServer6(String parameter, String parameter2, String parameter3, String parameter4, String parameter5, Callback callback6) {
        RequestBody body = new FormBody.Builder()
                .add("order", parameter)
                .add("date", parameter2)
                .add("loc", parameter3)
                .add("etc", parameter4)
                .add("code", parameter5)
                .build();
        Request request = new Request.Builder()
                .url("http://221.144.213.95/Buyorder.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback6);
    }
}
