// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bot_buy_orders_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BotBuyOrdersResponseModel _$BotBuyOrdersResponseModelFromJson(
        Map<String, dynamic> json) =>
    BotBuyOrdersResponseModel(
      botBuyOrders: (json['botBuyOrders'] as List<dynamic>?)
          ?.map((e) => BotBuyOrder.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$BotBuyOrdersResponseModelToJson(
        BotBuyOrdersResponseModel instance) =>
    <String, dynamic>{
      'botBuyOrders': instance.botBuyOrders,
    };

BotBuyOrder _$BotBuyOrderFromJson(Map<String, dynamic> json) => BotBuyOrder(
      id: json['id'] as int?,
      botId: json['botId'] as int?,
      tradingPair: json['tradingPair'] as String?,
      buyPrice: (json['buyPrice'] as num?)?.toDouble(),
      quantity: (json['quantity'] as num?)?.toDouble(),
      timestamp: json['timestamp'] == null
          ? null
          : DateTime.parse(json['timestamp'] as String),
    );

Map<String, dynamic> _$BotBuyOrderToJson(BotBuyOrder instance) =>
    <String, dynamic>{
      'id': instance.id,
      'botId': instance.botId,
      'tradingPair': instance.tradingPair,
      'buyPrice': instance.buyPrice,
      'quantity': instance.quantity,
      'timestamp': instance.timestamp?.toIso8601String(),
    };
