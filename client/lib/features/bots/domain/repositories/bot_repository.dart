import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';
import 'package:dartz/dartz.dart';

abstract class BotRepository{
  Future<Either<Failure, CreateBotResponseModel>> createBot(
      CreateBotParams createBotParams);
}