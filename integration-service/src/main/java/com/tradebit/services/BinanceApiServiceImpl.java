package com.tradebit.services;

import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.exceptions.InvalidSideException;
import com.tradebit.exceptions.InvalidTypeException;
import com.tradebit.models.OrderSide;
import com.tradebit.models.OrderType;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class BinanceApiServiceImpl implements BinanceApiService{
    private final OkHttpClient client;
    private static final String API_URL = "https://api.binance.com";
    private static final String HMAC_SHA256 = "HmacSHA256";

    public BinanceApiServiceImpl() {
        this.client = new OkHttpClient();
    }

    @Override
    public String getAccountData(String apiKey, String apiSecret){
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = "timestamp=" + timeStamp;
        String signature = hashHmac(queryString, apiSecret);

        HttpUrl url = buildRequestUrl(queryString, signature,  "/api/v3/account");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", apiKey)
                .build();

        try {
            return executeRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String buyCurrency(BinanceOrderDTO orderDTO, String apiKey, String apiSecret) {
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = buildQueryString(orderDTO, timeStamp);
        String signature = hashHmac(queryString, apiSecret);
        HttpUrl url = buildRequestUrl(queryString, signature, "/api/v3/order");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", apiKey)
                .post(RequestBody.create("", null)) // to indicate post request
                .build();

        try {
            return executeRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpUrl buildRequestUrl(String queryString, String signature, String endpoint) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL + endpoint).newBuilder();
        String[] queryParams = queryString.split("&");
        for (String param : queryParams) {
            String[] keyValuePair = param.split("=");
            urlBuilder.addQueryParameter(keyValuePair[0], keyValuePair[1]);
        }
        urlBuilder.addQueryParameter("signature", signature);
        return urlBuilder.build();
    }


    private String buildQueryString(BinanceOrderDTO orderDTO, long timeStamp) {
        return "symbol=" + orderDTO.getSymbol() +
                "&side=" + orderDTO.getSide().toString() +
                "&type=" + orderDTO.getType().toString() +
                "&quantity=" + orderDTO.getQuantity().toPlainString() +
                "&timestamp=" + timeStamp;
    }

    private String executeRequest(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        }
    }


    private String hashHmac(String data, String secret) {
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
            throw new RuntimeException("Unable to hash data", e);
        }
    }
}
