// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'params.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MakeOrderParams _$MakeOrderParamsFromJson(Map<String, dynamic> json) =>
    MakeOrderParams(
      orderDto: json['orderDTO'] == null
          ? null
          : OrderDto.fromJson(json['orderDTO'] as Map<String, dynamic>),
      linkDto: json['linkDTO'] == null
          ? null
          : LinkDto.fromJson(json['linkDTO'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$MakeOrderParamsToJson(MakeOrderParams instance) =>
    <String, dynamic>{
      'orderDTO': instance.orderDto?.toJson(),
      'linkDTO': instance.linkDto?.toJson(),
    };

LinkDto _$LinkDtoFromJson(Map<String, dynamic> json) => LinkDto(
      apiKey: json['apiKey'] as String?,
      secretApiKey: json['secretApiKey'] as String?,
    );

Map<String, dynamic> _$LinkDtoToJson(LinkDto instance) => <String, dynamic>{
      'apiKey': instance.apiKey,
      'secretApiKey': instance.secretApiKey,
    };

OrderDto _$OrderDtoFromJson(Map<String, dynamic> json) => OrderDto(
      symbol: json['symbol'] as String?,
      side: json['side'] as String?,
      type: json['type'] as String?,
      quantity: json['quantity'] as String?,
    );

Map<String, dynamic> _$OrderDtoToJson(OrderDto instance) => <String, dynamic>{
      'symbol': instance.symbol,
      'side': instance.side,
      'type': instance.type,
      'quantity': instance.quantity,
    };

CreateBotParams _$CreateBotParamsFromJson(Map<String, dynamic> json) =>
    CreateBotParams(
      name: json['name'] as String,
      buyThreshold: (json['buyThreshold'] as num).toDouble(),
      sellThreshold: (json['sellThreshold'] as num).toDouble(),
      takeProfitPercentage: (json['takeProfitPercentage'] as num).toDouble(),
      stopLossPercentage: (json['stopLossPercentage'] as num).toDouble(),
      tradeSize: (json['tradeSize'] as num).toDouble(),
      tradingPair: json['tradingPair'] as String,
    );

Map<String, dynamic> _$CreateBotParamsToJson(CreateBotParams instance) =>
    <String, dynamic>{
      'name': instance.name,
      'buyThreshold': instance.buyThreshold,
      'sellThreshold': instance.sellThreshold,
      'takeProfitPercentage': instance.takeProfitPercentage,
      'stopLossPercentage': instance.stopLossPercentage,
      'tradeSize': instance.tradeSize,
      'tradingPair': instance.tradingPair,
    };
