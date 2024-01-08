part of 'register_cubit.dart';

@freezed
abstract class RegisterState with _$RegisterState {
  const factory RegisterState.initial() = _Initial;
  const factory RegisterState.loading() = _Loading;
  const factory RegisterState.success(RegisterResponseEntity register) = _Success;
  const factory RegisterState.failure(String message) = _Failure;
}
