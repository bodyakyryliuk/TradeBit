// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'current_price_for_trading_pair_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CurrentPriceForTradingPairResponseModel
    _$CurrentPriceForTradingPairResponseModelFromJson(
            Map<String, dynamic> json) =>
        CurrentPriceForTradingPairResponseModel(
          symbol: json['symbol'] as String,
          price: json['price'] as String,
        );

Map<String, dynamic> _$CurrentPriceForTradingPairResponseModelToJson(
        CurrentPriceForTradingPairResponseModel instance) =>
    <String, dynamic>{
      'symbol': instance.symbol,
      'price': instance.price,
    };
