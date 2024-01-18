import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:dartz/dartz.dart';

abstract class AuthRepository {
  Future<Either<Failure, RegisterResponseEntity>> register(
      RegisterParams registerParams);

  Future<Either<Failure, LoginResponseEntity>> login(LoginParams loginParams);

  Future<Either<Failure, ResetPasswordResponseEntity>> resetPassword(
      ResetPasswordParams resetPasswordParams);

  Either<Failure, void> saveAccessToken(String? accessToken);

  Either<Failure, void> saveRefreshToken(String? refreshToken);

  Either<Failure, String?> getRefreshToken();
}
