import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/data/datasources/auth_local_datasource.dart';
import 'package:cointrade/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class AuthRepositoryImpl implements AuthRepository {
  final AuthRemoteDataSource authRemoteDatasource;
  final AuthLocalDataSource authLocalDataSource;

  AuthRepositoryImpl(this.authRemoteDatasource, this.authLocalDataSource);

  @override
  Future<Either<Failure, LoginResponseEntity>> login(
      LoginParams loginParams) async {
    try {
      final response = await authRemoteDatasource.login(loginParams);
      return Right(response.toEntity());
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, RegisterResponseEntity>> register(
      RegisterParams registerParams) async {
    try {
      final response = await authRemoteDatasource.register(registerParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, ResetPasswordResponseEntity>> resetPassword(
      ResetPasswordParams resetPasswordParams) async {
    try {
      final response =
          await authRemoteDatasource.resetPassword(resetPasswordParams);
      return Right(response.toEntity());
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Either<Failure, String?> getRefreshToken() {
    try {
      return Right(authLocalDataSource.getRefreshToken());
    } catch (e) {
      return const Left(StorageFailure('Unknown error occured'));
    }
  }

  @override
  Either<Failure, void> saveAccessToken(String? accessToken) {
    try {
      return Right(authLocalDataSource.saveAccessToken(accessToken));
    } catch (e) {
      return const Left(StorageFailure('Unknown error occured'));
    }
  }

  @override
  Either<Failure, void> saveRefreshToken(String? refreshToken) {
    try {
      return Right(authLocalDataSource.saveRefreshToken(refreshToken));
    } catch (e) {
      return const Left(StorageFailure('Unknown error occured'));
    }
  }
}
