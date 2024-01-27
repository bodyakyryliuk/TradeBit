package com.tradebit.models.order;

public enum OrderSide {
    BUY,
    SELL;

    public static boolean contains(String testOrderSide) {
        for (OrderSide side : OrderSide.values()) {
            if (side.name().equals(testOrderSide)) {
                return true;
            }
        }
        return false;
    }
}
