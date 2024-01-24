import 'package:json_annotation/json_annotation.dart';

part 'bots_response_model.g.dart';

class BotsResponseModel {
  final String? message;
  final String? status;
  final List<BotModel>? bots;

  BotsResponseModel({this.message, required this.status, required this.bots});

  factory BotsResponseModel.fromJson(Map<String, dynamic> json) {
    final List<dynamic>? botsJson = json['bots'];
    final List<BotModel> bots =
        botsJson?.map((botJson) => BotModel.fromJson(botJson)).toList() ?? [];

    return BotsResponseModel(
      message: json['message'],
      status: json['status'],
      bots: bots,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'message': message,
      'status': status,
      'bots': bots?.map((bot) => bot.toJson()).toList(),
    };
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
