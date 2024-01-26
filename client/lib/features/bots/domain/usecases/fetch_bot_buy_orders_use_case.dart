import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class FetchBotBuyOrdersUseCase
    implements UseCase<BotBuyOrdersResponseModel, BotBuyOrdersParams> {
  final BotRepository botRepository;

  FetchBotBuyOrdersUseCase(this.botRepository);

  @override
  Future<Either<Failure, BotBuyOrdersResponseModel>> call(
      BotBuyOrdersParams params) async {
    return await botRepository.fetchBotBuyOrders(params);
  }
}
