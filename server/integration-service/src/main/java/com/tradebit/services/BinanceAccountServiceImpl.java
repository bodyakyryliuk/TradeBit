package com.tradebit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.encryption.EncryptionUtil;
import com.tradebit.models.BinanceAccountLink;
import com.tradebit.models.TotalBalance;
import com.tradebit.models.wallet.CryptoBalance;
import com.tradebit.models.wallet.WalletInfo;
import com.tradebit.repositories.BinanceAccountLinkRepository;
import com.tradebit.repositories.TotalBalanceRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BinanceAccountServiceImpl implements BinanceAccountService{
    private final BinanceApiService binanceApiService;
    private final BinanceRequestServiceImpl binanceRequestService;
    private final BinanceResponseProcessingService responseProcessingService;
    private final TotalBalanceRepository totalBalanceRepository;
    private final BinanceAccountLinkRepository binanceAccountLinkRepository;
    private final EncryptionUtil encryptionUtil;

    private Map<String, Double> mapPricesToSymbols(JsonNode allPrices) {
        Map<String, Double> priceMap = new HashMap<>();
        for (JsonNode priceInfo : allPrices) {
            String symbol = priceInfo.get("symbol").asText();
            double price = priceInfo.get("price").asDouble();
            priceMap.put(symbol, price);
        }
        return priceMap;
    }

    private double calculateTotalBalance(List<CryptoBalance> balances, Map<String, Double> priceMap){
        double totalBalance = 0.0;
        for (CryptoBalance cryptoBalance : balances) {
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
    public JsonNode getTotalBalance(BinanceLinkDTO binanceLinkDTO) {
        WalletInfo walletInfo = getWallet(binanceLinkDTO);
        JsonNode allPricesResponse = binanceApiService.getCurrentPrices();
        Map<String, Double> priceMap = mapPricesToSymbols(allPricesResponse);
        double totalBalance = calculateTotalBalance(walletInfo.getBalances(), priceMap);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("balance", totalBalance);

        return responseNode;
    }

    @Override
    public JsonNode getAccountData(BinanceLinkDTO binanceLinkDTO){
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = "timestamp=" + timeStamp;
        String signature = binanceRequestService.hashHmac(queryString, binanceLinkDTO.getSecretApiKey());
        HttpUrl url = binanceRequestService.buildRequestUrl(queryString, signature,  "/api/v3/account");
        Request request = binanceRequestService.buildRequest(url, binanceLinkDTO.getApiKey(), "GET", null);

        return responseProcessingService.processResponse(binanceRequestService.executeRequest(request));
    }

    @Override
    public JsonNode getTotalBalanceHistory(BinanceLinkDTO binanceLinkDTO, int period) {
        LocalDateTime periodDateTime = LocalDateTime.now().minusHours(period);
        String apiKeyHash = encryptionUtil.hashApiKey(binanceLinkDTO.getApiKey());
        BinanceAccountLink binanceAccountLink = binanceAccountLinkRepository.findByApiKeyHash(apiKeyHash);
        String userId = binanceAccountLink.getUserId();
        List<TotalBalance> totalBalances = totalBalanceRepository.findAllByUserId(userId);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (TotalBalance balance : totalBalances) {
            LocalDateTime balanceTimeStamp = LocalDateTime.ofInstant(balance.getTimeStamp().toInstant(), ZoneId.systemDefault());
            if(balanceTimeStamp.isAfter(periodDateTime)) {
                ObjectNode balanceNode = mapper.createObjectNode();
                balanceNode.put("id", balance.getId());
                balanceNode.put("balance", balance.getTotalBalance());
                balanceNode.put("date", balance.getTimeStamp().toString());
                balanceNode.put("userId", balance.getUserId());
                arrayNode.add(balanceNode);
            }
        }

        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.set("balances", arrayNode);

        return responseNode;
    }

    @Override
    public JsonNode getAllOrders(BinanceLinkDTO binanceLinkDTO, String symbol) {
        long timeStamp = Instant.now().toEpochMilli();
        String queryString = "symbol=" + symbol + "&timestamp=" + timeStamp;
        String signature = binanceRequestService.hashHmac(queryString, binanceLinkDTO.getSecretApiKey());
        HttpUrl url = binanceRequestService.buildRequestUrl(queryString, signature, "/api/v3/myTrades");
        Request request = binanceRequestService.buildRequest(url, binanceLinkDTO.getApiKey(), "GET", null);

        return responseProcessingService.processResponse(binanceRequestService.executeRequest(request));
    }

    private CryptoBalance createCryptoBalanceFromJsonNode(JsonNode jsonBalance) {
        CryptoBalance balance = new CryptoBalance();
        balance.setAsset(jsonBalance.get("asset").asText());
        balance.setFree(jsonBalance.get("free").asDouble());
        balance.setLocked(jsonBalance.get("locked").asDouble());

        if (!balance.getAsset().equals("USDT")) {
            String tradingPair = balance.getAsset() + "USDT";
            balance.setPriceChange(binanceApiService.getPriceChange(tradingPair, 24));
        }else
            balance.setPriceChange(0.0);

        return balance;
    }


    @Override
    public WalletInfo getWallet(BinanceLinkDTO binanceLinkDTO) {
        JsonNode response = getAccountData(binanceLinkDTO);
        WalletInfo walletInfo = new WalletInfo();
        List<CryptoBalance> balances = new ArrayList<>();

        // get only the cryptocurrencies that user has
        JsonNode jsonBalances = response.get("balances");
        for (JsonNode jsonBalance : jsonBalances) {
            if(jsonBalance.get("free").asDouble() != 0.0) {
                balances.add(createCryptoBalanceFromJsonNode(jsonBalance));
            }
        }

        walletInfo.setBalances(balances);
        walletInfo.setCanTrade(response.get("canTrade").asBoolean());
        walletInfo.setCanWithdraw(response.get("canWithdraw").asBoolean());
        walletInfo.setCanDeposit(response.get("canDeposit").asBoolean());

        return walletInfo;
    }
}
