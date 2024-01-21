// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'historical_prices_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

HistoricalPrice _$HistoricalPriceFromJson(Map<String, dynamic> json) =>
    HistoricalPrice(
      openPrice: (json['openPrice'] as num?)?.toDouble(),
      highPrice: (json['highPrice'] as num?)?.toDouble(),
      lowPrice: (json['lowPrice'] as num?)?.toDouble(),
      closePrice: (json['closePrice'] as num?)?.toDouble(),
      volume: (json['volume'] as num?)?.toDouble(),
      timestamp: json['timestamp'] as int?,
    );

Map<String, dynamic> _$HistoricalPriceToJson(HistoricalPrice instance) =>
    <String, dynamic>{
      'openPrice': instance.openPrice,
      'highPrice': instance.highPrice,
      'lowPrice': instance.lowPrice,
      'closePrice': instance.closePrice,
      'volume': instance.volume,
      'timestamp': instance.timestamp,
    };
