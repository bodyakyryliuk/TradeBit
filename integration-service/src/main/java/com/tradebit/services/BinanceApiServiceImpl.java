package com.tradebit.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.dto.BinanceOrderDTO;
import com.tradebit.exceptions.BinanceRequestException;
import com.tradebit.exceptions.InsufficientBalanceException;
import com.tradebit.exceptions.InvalidQuantityException;
import com.tradebit.exceptions.UnexpectedException;
import com.tradebit.models.wallet.CryptoBalance;
import com.tradebit.models.wallet.WalletInfo;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BinanceApiServiceImpl implements BinanceApiService{
    //TODO: implement BinanceApiClient that will handle API calls
    //TODO: refactor code to multiple services
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
    public WalletInfo getWallet(BinanceLinkDTO binanceLinkDTO) {
        // Make the API call to Binance
        JsonNode response = getAccountData(binanceLinkDTO, "/api/v3/account");

        WalletInfo walletInfo = new WalletInfo();
        List<CryptoBalance> balances = new ArrayList<>();

        JsonNode jsonBalances = response.get("balances");
        for (JsonNode jsonBalance : jsonBalances) {
            if(jsonBalance.get("free").asDouble() != 0.0) {
                CryptoBalance balance = new CryptoBalance();
                balance.setAsset(jsonBalance.get("asset").asText());
                balance.setFree(jsonBalance.get("free").asDouble());
                balance.setLocked(jsonBalance.get("locked").asDouble());
                // TODO: refactor
                String tradingPair = "";
                if (!balance.getAsset().equals("USDT")) {
                    tradingPair = balance.getAsset() + "USDT";
                    balance.setPriceChange(getPriceChange(tradingPair, 24));
                }else
                    balance.setPriceChange(0.0);
                balances.add(balance);
            }
        }

        walletInfo.setBalances(balances);
        walletInfo.setCanTrade(response.get("canTrade").asBoolean());
        walletInfo.setCanWithdraw(response.get("canWithdraw").asBoolean());
        walletInfo.setCanDeposit(response.get("canDeposit").asBoolean());

        return walletInfo;
    }

    public JsonNode getCurrentPrices() {
        HttpUrl url = HttpUrl.parse(API_URL  + "/api/v3/ticker/price")
                .newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            return processResponse(executeRequest(request));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
        Method returns json node of all cryptocurrencies with USDT with prices
        [
            {
                "symbol": "ETHBTC",
                "price": "0.05367000"
            },
            {
                "symbol": "LTCBTC",
                "price": "0.00169800"
            },
        ]
     */
    @Override
    public JsonNode getAllCryptocurrenciesWithUSDT() {
        JsonNode allCryptos = getCurrentPrices();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode result = mapper.createArrayNode(); // This will hold the filtered results

        // Assuming allCryptos is an array of cryptocurrency data
        if (allCryptos.isArray()) {
            for (JsonNode cryptoNode : allCryptos) {
                String symbol = cryptoNode.get("symbol").asText();
                if (symbol.contains("USDT")) {
                    result.add(cryptoNode);
                }
            }
        }

        return result;
    }

    @Override
    public JsonNode getCurrentPriceForCrypto(String tradingPair) {
        HttpUrl url = HttpUrl.parse(API_URL  + "/api/v3/ticker/price?symbol=" + tradingPair)
                .newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();


        try {
            return processResponse(executeRequest(request));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public double getTotalBalance(BinanceLinkDTO binanceLinkDTO) {
        WalletInfo walletInfo = getWallet(binanceLinkDTO);
        JsonNode allPricesResponse = getCurrentPrices();
        Map<String, Double> priceMap = new HashMap<>();
        for (JsonNode priceInfo : allPricesResponse) {
            String symbol = priceInfo.get("symbol").asText();
            double price = priceInfo.get("price").asDouble();
            priceMap.put(symbol, price);
        }

        // Calculate total balance
        double totalBalance = 0.0;
        for (CryptoBalance cryptoBalance : walletInfo.getBalances()) {
            String symbol = cryptoBalance.getAsset();
            double balance = cryptoBalance.getFree();
            if (!symbol.equals("USDT")) {
                symbol += "USDT";
            }
            double price = priceMap.getOrDefault(symbol, 1.0);
            totalBalance += balance * price;
        }

        return totalBalance;
    }

    @Override
    public Double getPriceChange(String tradingPair, int period) {
        long currentTimeMillis = Instant.now().toEpochMilli();
        long periodAgoMillis = Instant.now().minusMillis(period * 3600000L).toEpochMilli();
        Double historicalPrice = getHistoricalPrice(tradingPair, periodAgoMillis);
        JsonNode jsonNode = getCurrentPriceForCrypto(tradingPair);
        Double currentPrice = jsonNode.get("price").asDouble();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

        String currentTimestamp = formatter.format(Instant.ofEpochMilli(currentTimeMillis));
        String periodAgoTimestamp = formatter.format(Instant.ofEpochMilli(periodAgoMillis));


        System.out.println(historicalPrice + "  historical " + periodAgoTimestamp);
        System.out.println(currentPrice + "  current " + currentTimestamp);

        if (!historicalPrice.isNaN() && !currentPrice.isNaN())
            return (currentPrice - historicalPrice) / historicalPrice * 100;
        else
            throw new BinanceRequestException("No data available for the specified time");
    }

    @Override
    public Double getHistoricalPrice(String tradingPair, long timestamp) {
        long adjustedTimestamp = timestamp - (timestamp % (3600000L));

        HttpUrl url = HttpUrl.parse(API_URL + "/api/v3/klines").newBuilder()
                .addEncodedQueryParameter("symbol", tradingPair)
                .addEncodedQueryParameter("interval", "1s")
                .addEncodedQueryParameter("startTime", String.valueOf(adjustedTimestamp))
                .addEncodedQueryParameter("limit", "1")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            JsonNode response = processResponse(executeRequest(request));
            if (response.isArray() && !response.isEmpty()) {
                return response.get(0).get(4).asDouble(); // Close price of the kline
            } else {
                throw new BinanceRequestException("No data available for the specified timestamp.");
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode getHistoricalPrices(String tradingPair, int period) {
        long endTimeMillis = Instant.now().toEpochMilli();
        long startTimeMillis = Instant.now().minusMillis(period * 3600000L).toEpochMilli();

        HttpUrl url = HttpUrl.parse(API_URL + "/api/v3/klines").newBuilder()
                .addEncodedQueryParameter("symbol", tradingPair)
                .addEncodedQueryParameter("interval", "1m")
                .addEncodedQueryParameter("startTime", String.valueOf(startTimeMillis))
                .addEncodedQueryParameter("endTime", String.valueOf(endTimeMillis))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            return processClosePrices(executeRequest(request));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode processClosePrices(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode closePricesNode = objectMapper.createArrayNode();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            if (rootNode.isArray()) {
                for (JsonNode klineNode : rootNode) {
                    // The close price is the 4th element in the kline array
                    JsonNode closePrice = klineNode.get(4);
                    closePricesNode.add(closePrice);
                }
            }

            return closePricesNode;
        } catch (JsonProcessingException e) {
            throw new UnexpectedException("Error processing JSON" + e);
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
