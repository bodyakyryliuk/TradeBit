import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/settings/data/models/link_binance_response_model.dart';
import 'package:cointrade/features/settings/data/models/top_up_code_response_model.dart';

class LinkBinanceRemoteDataSource {
  final DioClient _client;

  LinkBinanceRemoteDataSource(this._client);

  Future<LinkBinanceResponseModel> linkBinance(
      LinkBinanceParams linkBinanceParams) async {
    try {
      final response = await _client.postRequest(EndPoints.linkBinance,
          data: linkBinanceParams.toJson());
      final result = LinkBinanceResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  Future<TopUpCodeResponseModel> fetchTopUpCode(TopUpCodeParams topUpParams) async {
    try {
      final response = await _client.getRequest(EndPoints.topUpCode,
          data: topUpParams.toJson(),
          queryParameters: topUpParams.queryParamsToJson());
      final result = TopUpCodeResponseModel.fromJson(response.data);
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
