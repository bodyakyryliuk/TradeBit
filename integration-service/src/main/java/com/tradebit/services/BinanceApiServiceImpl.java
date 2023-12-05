package com.tradebit.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.exceptions.BinanceRequestException;
import com.tradebit.exceptions.InsufficientBalanceException;
import com.tradebit.exceptions.InvalidQuantityException;
import com.tradebit.exceptions.UnexpectedException;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Service
public class BinanceApiServiceImpl implements BinanceApiService{
    private final OkHttpClient client;
    private static final String API_URL = "https://api.binance.com";
    private static final String HMAC_SHA256 = "HmacSHA256";

    public BinanceApiServiceImpl() {
        this.client = new OkHttpClient();
    }

    @Override
    public JsonNode getAccountData(BinanceLinkDTO binanceLinkDTO, String endpoint){
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = "timestamp=" + timeStamp;
        String signature = hashHmac(queryString, binanceLinkDTO.getSecretApiKey());

        HttpUrl url = buildRequestUrl(queryString, signature,  endpoint);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", binanceLinkDTO.getApiKey())
                .build();

        try {
            String response = executeRequest(request);
            return processResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode executeOrder(BinanceOrderDTO orderDTO, String apiKey, String apiSecret, String endpoint) throws IOException {
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = buildQueryString(orderDTO, timeStamp);
        String signature = hashHmac(queryString, apiSecret);
        HttpUrl url = buildRequestUrl(queryString, signature, endpoint);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", apiKey)
                .post(RequestBody.create("", null)) // to indicate post request
                .build();

        String response = executeRequest(request);
        return processResponse(response);
    }

    @Override
    public JsonNode makeOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO) {
        try {
            return executeOrder(orderDTO, binanceLinkDTO.getApiKey(), binanceLinkDTO.getSecretApiKey(), "/api/v3/order");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode testNewOrder(BinanceOrderDTO orderDTO, BinanceLinkDTO binanceLinkDTO) {
        try {
            return executeOrder(orderDTO, binanceLinkDTO.getApiKey(), binanceLinkDTO.getSecretApiKey(), "/api/v3/order/test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol) {
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = "symbol=" + symbol + "&timestamp=" + timeStamp;
        String signature = hashHmac(queryString, binanceLinkDTO.getSecretApiKey());

        HttpUrl url = buildRequestUrl(queryString, signature,  "/api/v3/myTrades");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", binanceLinkDTO.getApiKey())
                .build();

        try {
            return processResponse(executeRequest(request));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode getWallet(BinanceLinkDTO binanceLinkDTO) {
        return getAccountData(binanceLinkDTO, "/api/v3/account");
    }

//    @Override
//    public Double getTotalBalance(BinanceLinkDTO binanceLinkDTO) {
//        return getAccountData(binanceLinkDTO, "/api/v3/account");
//    }

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

    private JsonNode processResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (response.startsWith("["))
                return objectMapper.readTree(response);

            JsonNode responseNode = objectMapper.readTree(response);

            if (responseNode.has("code") && responseNode.has("msg")) {
                String message = responseNode.get("msg").asText();
                switch (message) {
                    case "Filter failure: LOT_SIZE", "Filter failure: NOTIONAL" -> throw new InvalidQuantityException("Invalid quantity");
                    case "Account has insufficient balance for requested action." -> throw new InsufficientBalanceException();
                    default -> throw new BinanceRequestException(message);
                }
            }

            return responseNode;
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e.getMessage());
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
