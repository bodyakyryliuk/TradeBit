import 'package:json_annotation/json_annotation.dart';

part 'bots_response_model.g.dart';

class BotsResponseModel {
  final List<BotModel>? bots;

  BotsResponseModel({required this.bots});

  factory BotsResponseModel.fromJson(List<dynamic> json) {
    final List<BotModel> bots =
    json.map((botJson) => BotModel.fromJson(botJson)).toList();

    return BotsResponseModel(
      bots: bots,
    );
  }
}


@JsonSerializable()
class BotModel {
  @JsonKey(name: "id")
  final int? id;
  @JsonKey(name: "name")
  final String? name;
  @JsonKey(name: "buyThreshold")
  final double? buyThreshold;
  @JsonKey(name: "sellThreshold")
  final double? sellThreshold;
  @JsonKey(name: "takeProfitPercentage")
  final double? takeProfitPercentage;
  @JsonKey(name: "stopLossPercentage")
  final double? stopLossPercentage;
  @JsonKey(name: "tradeSize")
  final double? tradeSize;
  @JsonKey(name: "tradingPair")
  final String? tradingPair;
  @JsonKey(name: "userId")
  final String? userId;
  @JsonKey(name: "isReadyToBuy")
  final bool? isReadyToBuy;
  @JsonKey(name: "isReadyToSell")
  final bool? isReadyToSell;
  @JsonKey(name: "enabled")
  final bool? enabled;
  @JsonKey(name: "hidden")
  final bool? hidden;

  BotModel({
    this.id,
    this.name,
    this.buyThreshold,
    this.sellThreshold,
    this.takeProfitPercentage,
    this.stopLossPercentage,
    this.tradeSize,
    this.tradingPair,
    this.userId,
    this.isReadyToBuy,
    this.isReadyToSell,
    this.enabled,
    this.hidden,
  });

  factory BotModel.fromJson(Map<String, dynamic> json) =>
      _$BotModelFromJson(json);

  Map<String, dynamic> toJson() => _$BotModelToJson(this);
}
