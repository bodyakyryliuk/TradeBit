// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'link_binance_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

LinkBinanceResponseModel _$LinkBinanceResponseModelFromJson(
        Map<String, dynamic> json) =>
    LinkBinanceResponseModel(
      message: json['message'] as String?,
      status: json['status'] as String?,
      error: json['error'] as String?,
    );

Map<String, dynamic> _$LinkBinanceResponseModelToJson(
        LinkBinanceResponseModel instance) =>
    <String, dynamic>{
      'message': instance.message,
      'status': instance.status,
      'error': instance.error,
    };
