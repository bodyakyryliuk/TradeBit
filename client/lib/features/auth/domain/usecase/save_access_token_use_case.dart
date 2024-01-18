import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class SaveAccessTokenUseCase implements UseCase<void, String> {
  final AuthRepository authRepository;

  SaveAccessTokenUseCase(this.authRepository);

  @override
  Future<Either<Failure, void>> call(String? accessToken) async {
    return authRepository.saveAccessToken(accessToken);
  }
}