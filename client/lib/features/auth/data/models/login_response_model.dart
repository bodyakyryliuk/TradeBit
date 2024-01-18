import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:equatable/equatable.dart';

class LoginResponse extends Equatable {
  final String? message;
  final String? status;

  final String? accessToken;
  final String? refreshToken;

  const LoginResponse({
    this.message,
    this.status,
    this.accessToken,
    this.refreshToken,
  });

  LoginResponse.fromJson(dynamic json)
      : message = json["message"],
        status = json["status"],
        accessToken = json["access_token"],
        refreshToken = json["refresh_token"];

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map["message"] = message;
    map["status"] = status;
    map["access_token"] = accessToken;
    map["refresh_token"] = refreshToken;

    return map;
  }

  LoginResponseEntity toEntity() =>
      LoginResponseEntity(accessToken: accessToken, refreshToken: refreshToken);

  @override
  List<Object?> get props => [
        message,
        status,
        accessToken,
        refreshToken,
      ];
}
