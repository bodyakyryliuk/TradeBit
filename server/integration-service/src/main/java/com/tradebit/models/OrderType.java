package com.tradebit.models;

public enum OrderType {
    LIMIT,
    MARKET,
    STOP_LOSS,
    STOP_LOSS_LIMIT,
    TAKE_PROFIT,
    TAKE_PROFIT_LIMIT,
    LIMIT_MAKER;

    public static boolean contains(String testOrderType) {
        for (OrderType ty : OrderType.values()) {
            if (ty.name().equals(testOrderType)) {
                return true;
            }
        }
        return false;
    }
}
