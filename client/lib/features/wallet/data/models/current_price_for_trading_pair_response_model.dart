import 'package:freezed_annotation/freezed_annotation.dart';

part 'current_price_for_trading_pair_response_model.g.dart';

@JsonSerializable()
class CurrentPriceForTradingPairResponseModel{
  final String symbol;
  final String price;

  CurrentPriceForTradingPairResponseModel({required this.symbol, required this.price});

  factory CurrentPriceForTradingPairResponseModel.fromJson(Map<String, dynamic> json) =>
      _$CurrentPriceForTradingPairResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$CurrentPriceForTradingPairResponseModelToJson(this);
}