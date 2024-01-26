part of 'bot_buy_orders_cubit.dart';

@freezed
class BotBuyOrdersState with _$BotBuyOrdersState {
  const factory BotBuyOrdersState.initial() = _Initial;
  const factory BotBuyOrdersState.loading() = _Loading;
  const factory BotBuyOrdersState.success(BotBuyOrdersResponseModel botBuyOrdersResponseModel) = _Success;
  const factory BotBuyOrdersState.failure(String message) = _Failure;
}
