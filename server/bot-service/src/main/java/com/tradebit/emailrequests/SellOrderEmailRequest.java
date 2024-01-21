package com.tradebit.emailrequests;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SellOrderEmailRequest extends OrderEmailRequest {
    private double profit;
}
