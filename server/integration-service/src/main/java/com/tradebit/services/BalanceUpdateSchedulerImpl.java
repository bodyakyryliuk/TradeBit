package com.tradebit.services;

import com.tradebit.dto.BinanceLinkDTO;
import com.tradebit.encryption.EncryptionUtil;
import com.tradebit.models.BinanceAccountLink;
import com.tradebit.models.TotalBalance;
import com.tradebit.repositories.BinanceAccountLinkRepository;
import com.tradebit.repositories.TotalBalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceUpdateSchedulerImpl implements BalanceUpdateScheduler{
    private final BinanceAccountLinkRepository binanceAccountLinkRepository;
    private final EncryptionUtil encryptionUtil;
    private final BinanceAccountService binanceAccountService;
    private final TotalBalanceRepository totalBalanceRepository;

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";


    @Scheduled(fixedRate = 3600000)
    @Transactional
    @Override
    public void updateTotalBalances() {
        List<BinanceAccountLink> binanceLinks = binanceAccountLinkRepository.findAll();
        for (BinanceAccountLink binanceLink: binanceLinks){
            BinanceLinkDTO linkDTO = BinanceLinkDTO.builder()
                    .apiKey(encryptionUtil.decrypt(binanceLink.getApiKey()))
                    .secretApiKey(encryptionUtil.decrypt(binanceLink.getSecret_key()))
                    .build();

            double totalBalance = binanceAccountService.getTotalBalance(linkDTO).get("balance").asDouble();
            updateTotalBalanceInDatabase(binanceLink.getUserId(), totalBalance);
        }
    }

    private void updateTotalBalanceInDatabase(String userId, double totalBalance) {
        TotalBalance totalBalanceObj = TotalBalance.builder()
                .totalBalance(totalBalance)
                .timeStamp(new Date())
                .userId(userId)
                .build();

        totalBalanceRepository.save(totalBalanceObj);
    }
}
