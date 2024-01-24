import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';

class BotRemoteDataSource{

  final DioClient _client;

  BotRemoteDataSource(this._client);

  Future<CreateBotResponseModel> createBot(
      CreateBotParams createBotParams) async {
    try {
      final response = await _client.postRequest(EndPoints.createBot,
          data: createBotParams.toJson());
      final result = CreateBotResponseModel.fromJson(response.data);
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