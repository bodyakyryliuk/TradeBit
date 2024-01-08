import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:json_annotation/json_annotation.dart';

part 'reset_password_response_model.g.dart';

@JsonSerializable()
class ResetPasswordResponseModel {
  final String? success;
  final List<String>? message;

  final String? status;

  ResetPasswordResponseModel({this.success, this.message, this.status});

  factory ResetPasswordResponseModel.fromJson(Map<String, dynamic> json) =>
      _$ResetPasswordResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$ResetPasswordResponseModelToJson(this);

  ResetPasswordResponseEntity toEntity() => ResetPasswordResponseEntity(
        success: success,
        message: message,
        status: status,
      );
}
