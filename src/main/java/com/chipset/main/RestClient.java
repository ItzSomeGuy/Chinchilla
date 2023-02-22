package com.chipset.main;

import okhttp3.*;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;

public class RestClient {
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String userAgent = "Chinchilla/1.0 (JDA;) DBots/847520350291492974";

    public static void setClient(OkHttpClient client) {
        RestClient.client = client;
    }

    /**
     * Make a GET request
     * @param url the url to get
     * @return a response
     */
    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", userAgent)
                .build();

        return performRequest(request);
    }

    public static String get(String url, String key) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", key)
                .addHeader("User-Agent", userAgent)
                .build();

        return performRequest(request);
    }

    public static String performRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            String body;
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                body = "{}";
            } else {
                body = responseBody.string();
            }
            return body;
        } catch (SSLHandshakeException e) {
            System.out.println("Call to " + request.url() + " failed with SSLHandshakeException!");
            return "{error: 'SSLHandshakeException'}";
        } catch (IOException e) {
            return "{error: 'IOException'}";
        }
    }
}
