import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/current_price_for_trading_pair_response_model.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class FetchCurrentPriceForTradingPairUseCase extends UseCase<
    CurrentPriceForTradingPairResponseModel, CurrentPriceForTradingPairParams> {
  final WalletRepository _walletRepository;

  FetchCurrentPriceForTradingPairUseCase(this._walletRepository);

  @override
  Future<Either<Failure, CurrentPriceForTradingPairResponseModel>> call(
      CurrentPriceForTradingPairParams params) async {
    return await _walletRepository.fetchCurrentPriceForTradingPair(params);
  }
}
