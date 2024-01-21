import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class FetchAllCryptocurrenciesUseCase implements UseCase<AllCryptocurrenciesResponseModel, NoParams>{

  final WalletRepository walletRepository;

  FetchAllCryptocurrenciesUseCase(this.walletRepository);
  @override
  Future<Either<Failure, AllCryptocurrenciesResponseModel>> call(NoParams params) {
    return walletRepository.fetchAllCryptocurrencies();
  }

}