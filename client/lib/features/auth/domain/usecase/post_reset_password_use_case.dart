import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:dartz/dartz.dart';

class PostResetPasswordUseCase extends UseCase<ResetPasswordResponseEntity, ResetPasswordParams> {
  final AuthRepository _repository;

  PostResetPasswordUseCase(this._repository);

  @override
  Future<Either<Failure, ResetPasswordResponseEntity>> call(ResetPasswordParams params) {
    return _repository.resetPassword(params);
  }
}