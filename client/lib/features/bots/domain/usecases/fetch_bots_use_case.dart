import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class FetchBotsUseCase implements UseCase<BotsResponseModel, NoParams> {
  final BotRepository botRepository;

  FetchBotsUseCase(this.botRepository);

  @override
  Future<Either<Failure, BotsResponseModel>> call(NoParams params) async {
    return await botRepository.fetchBots();
  }
}