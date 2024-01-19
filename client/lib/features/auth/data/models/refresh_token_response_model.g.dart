// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'refresh_token_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RefreshTokenResponseModel _$RefreshTokenResponseModelFromJson(
        Map<String, dynamic> json) =>
    RefreshTokenResponseModel(
      accessToken: json['access_token'] as String?,
      refreshToken: json['refresh_token'] as String?,
      message: json['message'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$RefreshTokenResponseModelToJson(
        RefreshTokenResponseModel instance) =>
    <String, dynamic>{
      'message': instance.message,
      'status': instance.status,
      'access_token': instance.accessToken,
      'refresh_token': instance.refreshToken,
    };
