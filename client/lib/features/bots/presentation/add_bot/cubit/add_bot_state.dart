part of 'add_bot_cubit.dart';

@freezed
class AddBotState with _$AddBotState {
  const factory AddBotState.initial() = _Initial;
  const factory AddBotState.loading() = _Loading;
  const factory AddBotState.success(CreateBotResponseModel createBotResponseModel) = _Success;
  const factory AddBotState.failure(String message) = _Failure;
}
