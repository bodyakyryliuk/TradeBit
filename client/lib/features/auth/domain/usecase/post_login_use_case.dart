import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class PostLoginUseCase extends UseCase<LoginResponseEntity, LoginParams> {
  final AuthRepository _repository;

  PostLoginUseCase(this._repository);

  @override
  Future<Either<Failure, LoginResponseEntity>> call(LoginParams params) =>
      _repository.login(params);
}
