import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/home/data/datasources/wallet_remote_datasouce.dart';
import 'package:cointrade/features/home/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/home/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/home/data/models/wallet_response_model.dart';
import 'package:cointrade/features/home/domain/repositories/wallet_repository.dart';
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
  Future<Either<Failure, AllCryptocurrenciesResponseModel>> fetchAllCryptocurrencies() async{
    try {
      final response = await walletRemoteDataSource.fetchAllCryptocurrencies();
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }
}
