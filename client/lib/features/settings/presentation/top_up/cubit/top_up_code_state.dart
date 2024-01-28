part of 'top_up_code_cubit.dart';

@freezed
class TopUpCodeState with _$TopUpCodeState {
  const factory TopUpCodeState.initial() = _Initial;

  const factory TopUpCodeState.loading() = _Loading;

  const factory TopUpCodeState.success(
      TopUpCodeResponseModel topUpCodeResponseModel) = _Success;

  const factory TopUpCodeState.failure(String message) = _Failure;
}
