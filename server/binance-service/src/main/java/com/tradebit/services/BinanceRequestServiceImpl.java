package com.tradebit.services;

import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.exceptions.UnexpectedException;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class BinanceRequestServiceImpl implements BinanceRequestService{
    private final OkHttpClient client;
    private static final String API_URL = "https://api.binance.com";
    private static final String HMAC_SHA256 = "HmacSHA256";

    public BinanceRequestServiceImpl() {
        this.client = new OkHttpClient();
    }

    public String executeRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    public HttpUrl buildRequestUrl(String queryString, String signature, String endpoint) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL + endpoint).newBuilder();
        String[] queryParams = queryString.split("&");
        for (String param : queryParams) {
            String[] keyValuePair = param.split("=");
            urlBuilder.addQueryParameter(keyValuePair[0], keyValuePair[1]);
        }
        urlBuilder.addQueryParameter("signature", signature);
        return urlBuilder.build();
    }

    public HttpUrl buildRequestUrl(String endpoint) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL + endpoint).newBuilder();
        return urlBuilder.build();
    }

    @Override
    public HttpUrl buildKlineUrl(String tradingPair, String interval, long timeStamp) {
        HttpUrl url = HttpUrl.parse(API_URL + "/api/v3/klines").newBuilder()
                .addEncodedQueryParameter("symbol", tradingPair)
                .addEncodedQueryParameter("interval", interval)
                .addEncodedQueryParameter("startTime", String.valueOf(timeStamp))
                .addEncodedQueryParameter("limit", "1")
                .build();
        return url;
    }

    @Override
    public HttpUrl buildKlineUrl(String tradingPair, String interval, long startTime, long endTime) {
        HttpUrl url = HttpUrl.parse(API_URL + "/api/v3/klines").newBuilder()
                .addEncodedQueryParameter("symbol", tradingPair)
                .addEncodedQueryParameter("interval", interval)
                .addEncodedQueryParameter("startTime", String.valueOf(startTime))
                .addEncodedQueryParameter("endTime", String.valueOf(endTime))
                .addEncodedQueryParameter("limit", String.valueOf(1000))
                .build();
        return url;
    }

    public String buildQueryString(BinanceOrderDTO orderDTO, long timeStamp) {
        return "symbol=" + orderDTO.getSymbol() +
                "&side=" + orderDTO.getSide().toString() +
                "&type=" + orderDTO.getType().toString() +
                "&quantity=" + orderDTO.getQuantity().toPlainString() +
                "&timestamp=" + timeStamp;
    }

    public String hashHmac(String data, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            sha256_HMAC.init(secret_key);
            byte[] raw = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(2 * raw.length);
            for (byte b : raw) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new UnexpectedException("Unable to hash data: " + e);
        }
    }

    @Override
    public Request buildRequest(HttpUrl url, String apiKey, String method, RequestBody requestBody){
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", apiKey);

        switch (method.toUpperCase()) {
            case "GET" -> requestBuilder.get();
            case "POST" -> requestBuilder.post(requestBody);
            case "PUT" -> requestBuilder.put(requestBody);
            case "DELETE" -> requestBuilder.delete(requestBody);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return requestBuilder.build();
    }

    @Override
    public Request buildRequest(HttpUrl url) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        return requestBuilder.build();
    }

    @Override
    public Long getTimeMillisFromDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
