import 'package:json_annotation/json_annotation.dart';

part 'toggle_bot_enabled_response_model.g.dart';

@JsonSerializable()
class ToggleBotEnabledResponseModel {
  final bool enabled;
  final String? message;
  final String? status;

  ToggleBotEnabledResponseModel(
      {required this.enabled, required this.message, required this.status});

  factory ToggleBotEnabledResponseModel.fromJson(Map<String, dynamic> json) =>
      _$ToggleBotEnabledResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$ToggleBotEnabledResponseModelToJson(this);
}
