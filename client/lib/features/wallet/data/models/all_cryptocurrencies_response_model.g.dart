// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'all_cryptocurrencies_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CryptocurrencyModel _$CryptocurrencyModelFromJson(Map<String, dynamic> json) =>
    CryptocurrencyModel(
      symbol: json['symbol'] as String?,
      price: json['price'] as String?,
    );

Map<String, dynamic> _$CryptocurrencyModelToJson(
        CryptocurrencyModel instance) =>
    <String, dynamic>{
      'symbol': instance.symbol,
      'price': instance.price,
    };
