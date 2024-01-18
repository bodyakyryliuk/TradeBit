import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:dartz/dartz.dart';

class GetRefreshTokenUseCase implements SyncUseCase<String?, NoParams> {
  final AuthRepository authRepository;

  GetRefreshTokenUseCase(this.authRepository);

  @override
  Either<Failure, String?> call(NoParams params) {
    return authRepository.getRefreshToken();
  }
}
