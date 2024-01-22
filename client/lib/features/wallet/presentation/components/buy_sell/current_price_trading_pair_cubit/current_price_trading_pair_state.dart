part of 'current_price_trading_pair_cubit.dart';

@freezed
class CurrentPriceTradingPairState with _$CurrentPriceTradingPairState {
  const factory CurrentPriceTradingPairState.initial() = _Initial;
  const factory CurrentPriceTradingPairState.loading() = _Loading;
  const factory CurrentPriceTradingPairState.success(CurrentPriceForTradingPairResponseModel currentPriceForTradingPairResponseModel) = _Success;
  const factory CurrentPriceTradingPairState.failure(String message) = _Failure;
}
