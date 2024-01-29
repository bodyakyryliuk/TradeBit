part of 'all_trading_pairs_cubit.dart';

@freezed
class AllTradingPairsState with _$AllTradingPairsState {
  const factory AllTradingPairsState.initial() = _Initial;
  const factory AllTradingPairsState.loading() = _Loading;
  const factory AllTradingPairsState.success(TradingPairsResponseModel tradingPairsResponseModel) = _Success;
  const factory AllTradingPairsState.failure(String message) = _Failure;
}
