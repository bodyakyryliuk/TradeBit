import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_register_use_case.dart';
import 'package:dartz/dartz.dart';

abstract class AuthRepository{
  Future<Either<Failure, RegisterResponseEntity>> register(RegisterParams registerParams);
  Future<Either<Failure, LoginResponseEntity>> login(LoginParams loginParams);
}