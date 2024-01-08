import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'register_response_model.g.dart';

@JsonSerializable()
class RegisterResponseModel extends RegisterResponseEntity {
  final String message;
  final String status;

  RegisterResponseModel({required this.message,required this.status})
      : super(message: message, status: status);


  factory RegisterResponseModel.fromJson(Map<String, dynamic> json) =>
      _$RegisterResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$RegisterResponseModelToJson(this);
}
