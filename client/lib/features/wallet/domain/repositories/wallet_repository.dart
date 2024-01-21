import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/wallet/data/models/wallet_response_model.dart';
import 'package:dartz/dartz.dart';

abstract class WalletRepository {
  Future<Either<Failure, TotalBalanceResponseModel>> fetchTotalBalance(
      TotalBalanceParams totalBalanceParams);

  Future<Either<Failure, WalletResponseModel>> fetchWallet(
      WalletParams walletParams);

  Future<Either<Failure, HistoricalPricesResponseModel>> fetchHistoricalPrices(
      HistoricalPricesParams historicalPricesParams);

  Future<Either<Failure, AllCryptocurrenciesResponseModel>>
      fetchAllCryptocurrencies();
}
