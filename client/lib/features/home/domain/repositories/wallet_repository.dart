import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/home/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/home/data/models/wallet_response_model.dart';
import 'package:dartz/dartz.dart';

abstract class WalletRepository{
  Future<Either<Failure, TotalBalanceResponseModel>> fetchTotalBalance(
      TotalBalanceParams totalBalanceParams);

  Future<Either<Failure, WalletResponseModel>> fetchWallet(
      WalletParams walletParams);
}