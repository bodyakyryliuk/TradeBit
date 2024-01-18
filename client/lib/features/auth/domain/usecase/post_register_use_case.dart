import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class PostRegisterUseCase extends UseCase<RegisterResponseEntity, RegisterParams> {
  final AuthRepository _repository;

  PostRegisterUseCase(this._repository);

  @override
  Future<Either<Failure, RegisterResponseEntity>> call(RegisterParams params) {
    return _repository.register(params);
  }
}


