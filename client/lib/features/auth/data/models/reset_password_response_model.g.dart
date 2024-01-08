// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'reset_password_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ResetPasswordResponseModel _$ResetPasswordResponseModelFromJson(
        Map<String, dynamic> json) =>
    ResetPasswordResponseModel(
      success: json['success'] as String?,
      message:
          (json['message'] as List<dynamic>?)?.map((e) => e as String).toList(),
      status: json['status'] as String?,
    );

Map<String, dynamic> _$ResetPasswordResponseModelToJson(
        ResetPasswordResponseModel instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
      'status': instance.status,
    };
