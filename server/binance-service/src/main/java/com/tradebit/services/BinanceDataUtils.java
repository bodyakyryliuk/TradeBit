package com.tradebit.services;

public interface BinanceDataUtils {
    String getIntervalFromPeriod(int period);
    long getMillisFromInterval(String interval);

}
