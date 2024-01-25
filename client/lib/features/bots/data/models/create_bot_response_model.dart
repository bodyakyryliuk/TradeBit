import 'package:json_annotation/json_annotation.dart';

part 'create_bot_response_model.g.dart';

@JsonSerializable()
class CreateBotResponseModel {

  final String? message;

  final String? status;
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

  CreateBotResponseModel({
    this.status,
    this.message,
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

  factory CreateBotResponseModel.fromJson(Map<String, dynamic> json) => _$CreateBotResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$CreateBotResponseModelToJson(this);
}

