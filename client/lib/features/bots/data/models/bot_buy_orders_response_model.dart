import 'package:json_annotation/json_annotation.dart';

part 'bot_buy_orders_response_model.g.dart';

@JsonSerializable()
class BotBuyOrdersResponseModel {

  final List<BotBuyOrder>? botBuyOrders;

  BotBuyOrdersResponseModel({required this.botBuyOrders});

  factory BotBuyOrdersResponseModel.fromJson(List<dynamic> json) {
    final List<BotBuyOrder> botBuyOrders = json
        .map((botBuyOrderJson) => BotBuyOrder.fromJson(botBuyOrderJson))
        .toList();

    return BotBuyOrdersResponseModel(botBuyOrders: botBuyOrders);
  }
}

@JsonSerializable()
class BotBuyOrder {
  @JsonKey(name: "id")
  final int? id;
  @JsonKey(name: "botId")
  final int? botId;
  @JsonKey(name: "tradingPair")
  final String? tradingPair;
  @JsonKey(name: "buyPrice")
  final double? buyPrice;
  @JsonKey(name: "quantity")
  final double? quantity;
  @JsonKey(name: "timestamp")
  final DateTime? timestamp;

  BotBuyOrder({
    this.id,
    this.botId,
    this.tradingPair,
    this.buyPrice,
    this.quantity,
    this.timestamp,
  });

  factory BotBuyOrder.fromJson(Map<String, dynamic> json) =>
      _$BotBuyOrderFromJson(json);

  Map<String, dynamic> toJson() => _$BotBuyOrderToJson(this);
}
