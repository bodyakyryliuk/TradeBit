// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'create_bot_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CreateBotResponseModel _$CreateBotResponseModelFromJson(
        Map<String, dynamic> json) =>
    CreateBotResponseModel(
      message: json['message'] as String,
      status: json['status'] as String,
    );

Map<String, dynamic> _$CreateBotResponseModelToJson(
        CreateBotResponseModel instance) =>
    <String, dynamic>{
      'message': instance.message,
      'status': instance.status,
    };
