part of 'make_order_cubit.dart';

@freezed
class MakeOrderState with _$MakeOrderState {
  const factory MakeOrderState.initial() = _Initial;
  const factory MakeOrderState.loading() = _Loading;
  const factory MakeOrderState.success(MakeOrderResponseModel makeOrderResponseModel) = _Success;
  const factory MakeOrderState.failure(String message) = _Failure;
}
