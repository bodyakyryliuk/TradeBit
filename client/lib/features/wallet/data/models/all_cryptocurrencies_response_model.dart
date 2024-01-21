import 'dart:convert';

import 'package:json_annotation/json_annotation.dart';

part 'all_cryptocurrencies_response_model.g.dart';

class AllCryptocurrenciesResponseModel {
  final List<CryptocurrencyModel> cryptocurrencies;

  AllCryptocurrenciesResponseModel({required this.cryptocurrencies});

  factory AllCryptocurrenciesResponseModel.fromJson(List<dynamic> json) {
    final List<CryptocurrencyModel> cryptocurrencies = json
        .map((cryptoJson) => CryptocurrencyModel.fromJson(cryptoJson))
        .toList();

    return AllCryptocurrenciesResponseModel(cryptocurrencies: cryptocurrencies);
  }

  List<Map<String, dynamic>> toJson() {
    return cryptocurrencies.map((crypto) => crypto.toJson()).toList();
  }
}

@JsonSerializable()
class CryptocurrencyModel {
  @JsonKey(name: "symbol")
  final String? symbol;
  @JsonKey(name: "price")
  final String? price;

  CryptocurrencyModel({
    this.symbol,
    this.price,
  });

  factory CryptocurrencyModel.fromJson(Map<String, dynamic> json) =>
      _$CryptocurrencyModelFromJson(json);

  Map<String, dynamic> toJson() => _$CryptocurrencyModelToJson(this);
}
