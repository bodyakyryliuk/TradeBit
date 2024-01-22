import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/wallet/data/datasources/wallet_remote_datasouce.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/data/models/current_price_for_trading_pair_response_model.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/wallet/data/models/wallet_response_model.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class WalletRepositoryImpl implements WalletRepository {
  final WalletRemoteDataSource walletRemoteDataSource;

  WalletRepositoryImpl(this.walletRemoteDataSource);

  @override
  Future<Either<Failure, TotalBalanceResponseModel>> fetchTotalBalance(
      TotalBalanceParams totalBalanceParams) async {
    try {
      final response =
          await walletRemoteDataSource.fetchTotalBalance(totalBalanceParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, WalletResponseModel>> fetchWallet(
      WalletParams walletParams) async {
    try {
      final response = await walletRemoteDataSource.fetchWallet(walletParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, AllCryptocurrenciesResponseModel>>
      fetchAllCryptocurrencies() async {
    try {
      final response = await walletRemoteDataSource.fetchAllCryptocurrencies();
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, HistoricalPricesResponseModel>> fetchHistoricalPrices(
      HistoricalPricesParams historicalPricesParams) async {
    try {
      final response = await walletRemoteDataSource
          .fetchHistoricalPrices(historicalPricesParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, CurrentPriceForTradingPairResponseModel>>
      fetchCurrentPriceForTradingPair(
          CurrentPriceForTradingPairParams
              currentPriceForTradingPairParams) async {
    try {
      final response = await walletRemoteDataSource
          .fetchCurrentPriceForTradingPair(currentPriceForTradingPairParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }
}
