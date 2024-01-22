package com.tradebit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

/*
Trading Pair (tradingPair):
 parameter specifies the asset pair that the trading bot will trade.
 For example, this could be something like "BTCUSDT" for Bitcoin against USDT (Tether).

Buy Threshold (buyThreshold):
This is a percentage or price level at which the bot will consider buying the asset.
It could represent a percentage drop from the asset's recent high or a specific price level.
For example, if the buy threshold is set to 5%, the bot might consider buying when the price drops 5% below a recent high.

Sell Threshold (sellThreshold):
This is a percentage or price level at which the bot will consider selling the asset.
It could represent a percentage increase from the buy price or a specific price level.
For example, if the sell threshold is set to 10%, the bot might consider selling when the price increases by 10% from the buy price.

Take Profit Percentage (takeProfitPercentage):
This parameter sets the profit level at which the bot will automatically sell the asset to lock in gains.
For example, if it's set to 20%, the bot will sell when the price reaches a 20% profit from the buy price.

Stop Loss Percentage (stopLossPercentage):
This parameter sets the maximum allowable loss percentage before the bot sells the asset to limit losses.
For example, if it's set to 10%, the bot will sell if the price drops by 10% from the buy price to limit potential losses.

Trade Size (tradeSize):
This parameter specifies the size of each trade, which could be the quantity of the asset to buy or sell in each transaction.
It can be a fixed quantity or a percentage of the total portfolio size.
 */
public class BotDTO {
    @NotEmpty(message = "Name must not be empty")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Name must contain only letters and digits")
    private String name;

    @NotNull(message = "Buy threshold must not be null")
    @Positive(message = "Buy threshold must be positive")
    private Double buyThreshold;

    @NotNull(message = "Sell threshold must not be null")
    @Positive(message = "Sell threshold must be positive")
    private Double sellThreshold;

    @NotNull(message = "Take profit percentage must not be null")
    @Positive(message = "Take profit percentage must be positive")
    private Double takeProfitPercentage;

    @NotNull(message = "Stop loss percentage must not be null")
    @Positive(message = "Stop loss percentage must be positive")
    private Double stopLossPercentage;

    @NotNull(message = "Trade size must not be null")
    @Positive(message = "Trade size must be positive")
    private Double tradeSize;

    @NotEmpty(message = "Trading pair must not be empty")
    private String tradingPair;
}
