import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/trading_pairs_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class FetchTradingPairsUseCase
    implements UseCase<TradingPairsResponseModel, NoParams> {
  final BotRepository botRepository;

  FetchTradingPairsUseCase(this.botRepository);

  @override
  Future<Either<Failure, TradingPairsResponseModel>> call(
      NoParams params) async {
    return await botRepository.fetchTradingPairs();
  }
}
