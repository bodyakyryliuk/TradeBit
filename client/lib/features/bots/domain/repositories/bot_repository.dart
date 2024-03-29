import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:cointrade/features/bots/data/models/bot_sell_orders_response_model.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';
import 'package:cointrade/features/bots/data/models/predictions_response_model.dart';
import 'package:cointrade/features/bots/data/models/toggle_bot_enabled_response_model.dart';
import 'package:cointrade/features/bots/data/models/trading_pairs_response_model.dart';
import 'package:dartz/dartz.dart';

abstract class BotRepository {
  Future<Either<Failure, CreateBotResponseModel>> createBot(
      CreateBotParams createBotParams);

  Future<Either<Failure, BotsResponseModel>> fetchBots();

  Future<Either<Failure, bool>> toggleBotEnabled(
      ToggleBotEnabledParams toggleBotEnabledParams);

  Future<Either<Failure, void>> deleteBot(DeleteBotParams deleteBotParams);

  Future<Either<Failure, BotBuyOrdersResponseModel>> fetchBotBuyOrders(
      BotBuyOrdersParams botBuyOrdersParams);

  Future<Either<Failure, BotSellOrdersResponseModel>> fetchBotSellOrders(
      BotSellOrdersParams botSellOrdersParams);

  Future<Either<Failure, PredictionsResponseModel>> fetchPredictions(
      PredictionsParams predictionsParams);

  Future<Either<Failure, TradingPairsResponseModel>> fetchTradingPairs();
}
