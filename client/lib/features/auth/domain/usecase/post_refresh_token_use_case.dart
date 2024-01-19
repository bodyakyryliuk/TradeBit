import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/data/models/refresh_token_response_model.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class PostRefreshTokenUseCase implements UseCase<RefreshTokenResponseModel, RefreshTokenParams> {
  final AuthRepository repository;

  PostRefreshTokenUseCase(this.repository);

  @override
  Future<Either<Failure, RefreshTokenResponseModel>> call(RefreshTokenParams params) async {
    return await repository.refreshToken(params);
  }
}