import 'package:json_annotation/json_annotation.dart';

part 'historical_prices_response_model.g.dart';

class HistoricalPricesResponseModel {
  final List<HistoricalPrice> historicalPrices;

  HistoricalPricesResponseModel({required this.historicalPrices});

  factory HistoricalPricesResponseModel.fromJson(List<dynamic> json) {
    final List<HistoricalPrice> historicalPrices =
        json.map((cryptoJson) => HistoricalPrice.fromJson(cryptoJson)).toList();

    return HistoricalPricesResponseModel(historicalPrices: historicalPrices);
  }
}

@JsonSerializable()
class HistoricalPrice {
  @JsonKey(name: "openPrice")
  final double? openPrice;
  @JsonKey(name: "highPrice")
  final double? highPrice;
  @JsonKey(name: "lowPrice")
  final double? lowPrice;
  @JsonKey(name: "closePrice")
  final double? closePrice;
  @JsonKey(name: "volume")
  final double? volume;
  @JsonKey(name: "timestamp")
  final int? timestamp;

  HistoricalPrice({
    this.openPrice,
    this.highPrice,
    this.lowPrice,
    this.closePrice,
    this.volume,
    this.timestamp,
  });

  factory HistoricalPrice.fromJson(Map<String, dynamic> json) => _$HistoricalPriceFromJson(json);

  Map<String, dynamic> toJson() => _$HistoricalPriceToJson(this);
}

