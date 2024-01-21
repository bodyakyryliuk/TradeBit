import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class FetchHistoricalPricesUseCase
    extends UseCase<HistoricalPricesResponseModel, HistoricalPricesParams> {
  final WalletRepository repository;

  FetchHistoricalPricesUseCase(this.repository);

  @override
  Future<Either<Failure, HistoricalPricesResponseModel>> call(
      HistoricalPricesParams params) {
    return repository.fetchHistoricalPrices(params);
  }
}
