part of 'connect_binance_cubit.dart';

@freezed
class ConnectBinanceState with _$ConnectBinanceState {
  const factory ConnectBinanceState.initial() = _Initial;
  const factory ConnectBinanceState.loading() = _Loading;
  const factory ConnectBinanceState.success(LinkBinanceResponseEntity responseEntity) = _Success;
  const factory ConnectBinanceState.failure(String message) = _Failure;
}
