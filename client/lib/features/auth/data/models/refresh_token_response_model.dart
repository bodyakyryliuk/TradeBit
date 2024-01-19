import 'package:flutter/cupertino.dart';
import 'package:json_annotation/json_annotation.dart';

part 'refresh_token_response_model.g.dart';

@JsonSerializable()
class RefreshTokenResponseModel {
  final String? message;
  final String? status;

  @JsonKey(name:'access_token')
  final String? accessToken;

  @JsonKey(name:'refresh_token')
  final String? refreshToken;

  RefreshTokenResponseModel(
      {this.accessToken, this.refreshToken, this.message, this.status});

  factory RefreshTokenResponseModel.fromJson(Map<String, dynamic> json) =>
      _$RefreshTokenResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$RefreshTokenResponseModelToJson(this);
}
