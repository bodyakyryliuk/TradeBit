// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'create_bot_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CreateBotResponseModel _$CreateBotResponseModelFromJson(
        Map<String, dynamic> json) =>
    CreateBotResponseModel(
      status: json['status'] as String?,
      message: json['message'] as String?,
      id: json['id'] as int?,
      name: json['name'] as String?,
      buyThreshold: (json['buyThreshold'] as num?)?.toDouble(),
      sellThreshold: (json['sellThreshold'] as num?)?.toDouble(),
      takeProfitPercentage: (json['takeProfitPercentage'] as num?)?.toDouble(),
      stopLossPercentage: (json['stopLossPercentage'] as num?)?.toDouble(),
      tradeSize: (json['tradeSize'] as num?)?.toDouble(),
      tradingPair: json['tradingPair'] as String?,
      userId: json['userId'] as String?,
      isReadyToBuy: json['isReadyToBuy'] as bool?,
      isReadyToSell: json['isReadyToSell'] as bool?,
      enabled: json['enabled'] as bool?,
      hidden: json['hidden'] as bool?,
    );

Map<String, dynamic> _$CreateBotResponseModelToJson(
        CreateBotResponseModel instance) =>
    <String, dynamic>{
      'message': instance.message,
      'status': instance.status,
      'id': instance.id,
      'name': instance.name,
      'buyThreshold': instance.buyThreshold,
      'sellThreshold': instance.sellThreshold,
      'takeProfitPercentage': instance.takeProfitPercentage,
      'stopLossPercentage': instance.stopLossPercentage,
      'tradeSize': instance.tradeSize,
      'tradingPair': instance.tradingPair,
      'userId': instance.userId,
      'isReadyToBuy': instance.isReadyToBuy,
      'isReadyToSell': instance.isReadyToSell,
      'enabled': instance.enabled,
      'hidden': instance.hidden,
    };
