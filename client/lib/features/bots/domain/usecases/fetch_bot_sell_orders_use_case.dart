import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bot_sell_orders_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class FetchBotSellOrdersUseCase
    implements UseCase<BotSellOrdersResponseModel, BotSellOrdersParams> {
  final BotRepository botRepository;

  FetchBotSellOrdersUseCase(this.botRepository);

  @override
  Future<Either<Failure, BotSellOrdersResponseModel>> call(
      BotSellOrdersParams params) async {
    return await botRepository.fetchBotSellOrders(params);
  }
}
