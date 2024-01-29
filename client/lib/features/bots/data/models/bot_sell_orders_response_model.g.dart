// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bot_sell_orders_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BotSellOrder _$BotSellOrderFromJson(Map<String, dynamic> json) => BotSellOrder(
      id: json['id'] as int?,
      buyOrderId: json['buyOrderId'] as int?,
      botId: json['botId'] as int?,
      tradingPair: json['tradingPair'] as String?,
      sellPrice: (json['sellPrice'] as num?)?.toDouble(),
      quantity: (json['quantity'] as num?)?.toDouble(),
      profit: (json['profit'] as num?)?.toDouble(),
      timestamp: json['timestamp'] == null
          ? null
          : DateTime.parse(json['timestamp'] as String),
    );

Map<String, dynamic> _$BotSellOrderToJson(BotSellOrder instance) =>
    <String, dynamic>{
      'id': instance.id,
      'buyOrderId': instance.buyOrderId,
      'botId': instance.botId,
      'tradingPair': instance.tradingPair,
      'sellPrice': instance.sellPrice,
      'quantity': instance.quantity,
      'profit': instance.profit,
      'timestamp': instance.timestamp?.toIso8601String(),
    };
