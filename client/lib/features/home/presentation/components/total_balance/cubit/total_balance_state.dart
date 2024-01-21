part of 'total_balance_cubit.dart';

@freezed
class TotalBalanceState with _$TotalBalanceState {
  const factory TotalBalanceState.initial() = _Initial;
  const factory TotalBalanceState.loading() = _Loading;
  const factory TotalBalanceState.success(TotalBalanceResponseModel totalBalanceResponseModel) = _Success;
  const factory TotalBalanceState.failure(String message) = _Failure;
}
