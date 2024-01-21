import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/home/data/models/wallet_response_model.dart';
import 'package:cointrade/features/home/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class FetchWalletUseCase implements UseCase<WalletResponseModel, WalletParams> {
  final WalletRepository walletRepository;

  FetchWalletUseCase(this.walletRepository);

  @override
  Future<Either<Failure, WalletResponseModel>> call(WalletParams params) {
    return walletRepository.fetchWallet(params);
  }
}
