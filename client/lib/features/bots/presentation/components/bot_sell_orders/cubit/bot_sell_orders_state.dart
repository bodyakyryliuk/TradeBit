part of 'bot_sell_orders_cubit.dart';

@freezed
class BotSellOrdersState with _$BotSellOrdersState {
  const factory BotSellOrdersState.initial() = _Initial;
  const factory BotSellOrdersState.loading() = _Loading;
  const factory BotSellOrdersState.success(BotSellOrdersResponseModel botSellOrdersResponseModel) = _Success;
  const factory BotSellOrdersState.failure(String message) = _Failure;
}
