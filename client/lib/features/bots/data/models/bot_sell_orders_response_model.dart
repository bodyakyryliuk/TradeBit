import 'package:json_annotation/json_annotation.dart';

part 'bot_sell_orders_response_model.g.dart';

class BotSellOrdersResponseModel {
  final List<BotSellOrder>? botSellOrders;

  BotSellOrdersResponseModel({required this.botSellOrders});

  factory BotSellOrdersResponseModel.fromJson(List<dynamic> json) {
    final List<BotSellOrder> botSellOrders = json
        .map((botSellOrderJson) => BotSellOrder.fromJson(botSellOrderJson))
        .toList();

    return BotSellOrdersResponseModel(botSellOrders: botSellOrders);
  }
}

@JsonSerializable()
class BotSellOrder {
  @JsonKey(name: "id")
  final int? id;
  @JsonKey(name: "buyOrderId")
  final int? buyOrderId;
  @JsonKey(name: "botId")
  final int? botId;
  @JsonKey(name: "tradingPair")
  final String? tradingPair;
  @JsonKey(name: "sellPrice")
  final double? sellPrice;
  @JsonKey(name: "quantity")
  final double? quantity;
  @JsonKey(name: "profit")
  final double? profit;
  @JsonKey(name: "timestamp")
  final DateTime? timestamp;

  BotSellOrder({
    this.id,
    this.buyOrderId,
    this.botId,
    this.tradingPair,
    this.sellPrice,
    this.quantity,
    this.profit,
    this.timestamp,
  });

  factory BotSellOrder.fromJson(Map<String, dynamic> json) =>
      _$BotSellOrderFromJson(json);

  Map<String, dynamic> toJson() => _$BotSellOrderToJson(this);
}
