package com.tradebit.services;

import com.tradebit.dto.BinanceOrderDTO;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public interface BinanceRequestService {
    String executeRequest(Request request);
    HttpUrl buildRequestUrl(String queryString, String signature, String endpoint);
    HttpUrl buildRequestUrl(String endpoint);
    HttpUrl buildKlineUrl(String tradingPair, String interval, long timeStamp);
    HttpUrl buildKlineUrl(String tradingPair, String interval, long startTime, long endTime);
    String buildQueryString(BinanceOrderDTO orderDTO, long timeStamp);
    String hashHmac(String data, String secret);
    Request buildRequest(HttpUrl url, String apiKey, String method, RequestBody requestBody);
    Request buildRequest(HttpUrl url);
    Long getTimeMillisFromDate(String date);
}
