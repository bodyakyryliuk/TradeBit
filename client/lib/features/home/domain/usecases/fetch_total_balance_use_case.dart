import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/home/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/home/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class FetchTotalBalanceUseCase implements UseCase<TotalBalanceResponseModel, TotalBalanceParams>{

  final WalletRepository walletRepository;

  FetchTotalBalanceUseCase(this.walletRepository);
  @override
  Future<Either<Failure, TotalBalanceResponseModel>> call(TotalBalanceParams params) {
    return walletRepository.fetchTotalBalance(params);
  }

}