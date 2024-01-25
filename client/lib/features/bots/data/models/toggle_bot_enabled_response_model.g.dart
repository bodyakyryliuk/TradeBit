// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'toggle_bot_enabled_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ToggleBotEnabledResponseModel _$ToggleBotEnabledResponseModelFromJson(
        Map<String, dynamic> json) =>
    ToggleBotEnabledResponseModel(
      enabled: json['enabled'] as bool,
      message: json['message'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$ToggleBotEnabledResponseModelToJson(
        ToggleBotEnabledResponseModel instance) =>
    <String, dynamic>{
      'enabled': instance.enabled,
      'message': instance.message,
      'status': instance.status,
    };
