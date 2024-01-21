part of 'historical_prices_cubit.dart';

@freezed
class HistoricalPricesState with _$HistoricalPricesState {
  const factory HistoricalPricesState.initial() = _Initial;
  const factory HistoricalPricesState.loading() = _Loading;
  const factory HistoricalPricesState.success(HistoricalPricesResponseModel historicalPricesResponseModel) = _Success;
  const factory HistoricalPricesState.failure(String message) = _Failure;
}
