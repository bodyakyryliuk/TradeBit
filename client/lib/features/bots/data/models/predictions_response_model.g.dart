// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'predictions_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Prediction _$PredictionFromJson(Map<String, dynamic> json) => Prediction(
      id: json['id'] as String?,
      tradingPair: json['tradingPair'] as String?,
      predictedPrice: (json['predictedPrice'] as num?)?.toDouble(),
      timestamp: json['timestamp'] == null
          ? null
          : DateTime.parse(json['timestamp'] as String),
    );

Map<String, dynamic> _$PredictionToJson(Prediction instance) =>
    <String, dynamic>{
      'id': instance.id,
      'tradingPair': instance.tradingPair,
      'predictedPrice': instance.predictedPrice,
      'timestamp': instance.timestamp?.toIso8601String(),
    };
