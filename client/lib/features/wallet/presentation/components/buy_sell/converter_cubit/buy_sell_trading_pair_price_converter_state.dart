part of 'buy_sell_trading_pair_price_converter_cubit.dart';

@freezed
class BuySellTradingPairPriceConverterState
    with _$BuySellTradingPairPriceConverterState {
  const factory BuySellTradingPairPriceConverterState.initial() = _Initial;

  const factory BuySellTradingPairPriceConverterState.updateAmount(
      double amount) = _UpdateAmount;
  const factory BuySellTradingPairPriceConverterState.updateTotal(
      double amount) = _UpdateTotal;
}
