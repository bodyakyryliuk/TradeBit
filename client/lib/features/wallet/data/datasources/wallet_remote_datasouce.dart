import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/data/models/current_price_for_trading_pair_response_model.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/data/models/make_order_response_model.dart';
import 'package:cointrade/features/wallet/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/wallet/data/models/wallet_response_model.dart';

import '../../../../core/params/params.dart';

class WalletRemoteDataSource {
  final DioClient _client;

  WalletRemoteDataSource(this._client);

  Future<TotalBalanceResponseModel> fetchTotalBalance(
      TotalBalanceParams totalBalanceParams) async {
    try {
      final response = await _client.getRequest(EndPoints.totalBalance,
          data: totalBalanceParams.toJson());
      final result = TotalBalanceResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<WalletResponseModel> fetchWallet(WalletParams walletParams) async {
    try {
      final response = await _client.getRequest(EndPoints.wallet,
          data: walletParams.toJson());
      final result = WalletResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<HistoricalPricesResponseModel> fetchHistoricalPrices(
      HistoricalPricesParams historicalPricesParams) async {
    try {
      final response = await _client.getRequest(
        '${EndPoints.historicalPrices}/${historicalPricesParams.currencyPair}?period=${historicalPricesParams.period}',
      );
      final result = HistoricalPricesResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException('Error fetching historical prices');
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<CurrentPriceForTradingPairResponseModel>
      fetchCurrentPriceForTradingPair(
          CurrentPriceForTradingPairParams
              currentPriceForTradingPairParams) async {
    try {
      final response = await _client.getRequest(
        EndPoints.currentPriceForTradingPair,
        queryParameters: currentPriceForTradingPairParams.toJson(),
      );
      final result =
          CurrentPriceForTradingPairResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException('Error fetching current price for trading pair');
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<AllCryptocurrenciesResponseModel> fetchAllCryptocurrencies() async {
    try {
      final response = await _client.getRequest(
        EndPoints.allCryptocurrencies,
      );
      final result = AllCryptocurrenciesResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException('Error fetching all cryptocurrencies');
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<MakeOrderResponseModel>
  makeOrder(
      MakeOrderParams makeOrderParams) async {
    try {
      final response = await _client.postRequest(
        EndPoints.makeOrder,
        data: makeOrderParams.toJson(),
      );
      final result =
      MakeOrderResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }
}
