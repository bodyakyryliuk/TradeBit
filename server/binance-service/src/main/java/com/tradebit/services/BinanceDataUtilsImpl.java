package com.tradebit.services;

import org.springframework.stereotype.Service;

@Service
public class BinanceDataUtilsImpl implements BinanceDataUtils{
    @Override
    public String getIntervalFromPeriod(int period) {
        int totalMinutes = period * 60;

        if (totalMinutes <= 1000) {
            return "1m";
        } else if (totalMinutes <= 3000) {
            return "3m";
        } else if (totalMinutes <= 5000) {
            return "5m";
        } else if (totalMinutes <= 15000) {
            return "15m";
        } else if (totalMinutes <= 30000) {
            return "30m";
        } else if (totalMinutes <= 60000) {
            return "1h";
        } else if (totalMinutes <= 120000) {
            return "2h";
        } else if (totalMinutes <= 240000) {
            return "4h";
        } else if (totalMinutes <= 480000) {
            return "8h";
        } else if (totalMinutes <= 720000) {
            return "12h";
        } else if (totalMinutes <= 1440000) {
            return "1d";
        } else if (totalMinutes <= 4320000) {
            return "3d";
        } else {
            return "1w";
        }
    }

    @Override
    public long getMillisFromInterval(String interval) {
        int value = Integer.parseInt(interval.substring(0, interval.length() - 1));
        char unit = interval.charAt(interval.length() - 1);

        switch (unit) {
            case 'm':
                return value * 60 * 1000L; // minutes to milliseconds
            case 'h':
                return value * 60 * 60 * 1000L; // hours to milliseconds
            case 'd':
                return value * 24 * 60 * 60 * 1000L; // days to milliseconds
            case 'w':
                return value * 7 * 24 * 60 * 60 * 1000L; // weeks to milliseconds
        }
        return 0L;
    }
}
