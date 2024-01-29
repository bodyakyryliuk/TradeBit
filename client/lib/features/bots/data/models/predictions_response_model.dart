import 'package:json_annotation/json_annotation.dart';

part 'predictions_response_model.g.dart';

class PredictionsResponseModel{
  final List<Prediction> predictions;

  PredictionsResponseModel({required this.predictions});

  factory PredictionsResponseModel.fromJson(List<dynamic> json) {
    final List<Prediction> predictions = json
        .map((botBuyOrderJson) => Prediction.fromJson(botBuyOrderJson))
        .toList();

    return PredictionsResponseModel(predictions: predictions);
  }

}
@JsonSerializable()
class Prediction {
  @JsonKey(name: "id")
  final String? id;
  @JsonKey(name: "tradingPair")
  final String? tradingPair;
  @JsonKey(name: "predictedPrice")
  final double? predictedPrice;
  @JsonKey(name: "timestamp")
  final DateTime? timestamp;

  Prediction({
    this.id,
    this.tradingPair,
    this.predictedPrice,
    this.timestamp,
  });

  factory Prediction.fromJson(Map<String, dynamic> json) => _$PredictionFromJson(json);

  Map<String, dynamic> toJson() => _$PredictionToJson(this);
}
