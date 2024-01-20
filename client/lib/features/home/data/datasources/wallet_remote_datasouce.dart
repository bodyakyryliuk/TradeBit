import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/features/home/data/models/total_balance_response_model.dart';

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
}
