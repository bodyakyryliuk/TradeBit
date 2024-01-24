import 'package:json_annotation/json_annotation.dart';

part 'create_bot_response_model.g.dart';

@JsonSerializable()
class CreateBotResponseModel {
  final String message;
  final String status;

  CreateBotResponseModel({required this.message, required this.status});

  factory CreateBotResponseModel.fromJson(Map<String, dynamic> json) =>
      _$CreateBotResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$CreateBotResponseModelToJson(this);
}
