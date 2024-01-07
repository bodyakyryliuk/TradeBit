import 'package:cointrade/core/error/failures.dart';
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

class RegisterParams {
  final String email;
  final String password;
  final String firstName;
  final String lastName;

  RegisterParams(
      {required this.firstName,
      required this.lastName,
      required this.email,
      required this.password});

  Map<String, dynamic> toJson() => {
        "email": email,
        "password": password,
        "firstname": firstName,
        "lastname": lastName,
      };
}
