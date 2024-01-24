import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class CreateBotUseCase implements UseCase<CreateBotResponseModel, CreateBotParams> {
  final BotRepository botRepository;

  CreateBotUseCase(this.botRepository);

  @override
  Future<Either<Failure, CreateBotResponseModel>> call(CreateBotParams params) async {
    return await botRepository.createBot(params);
  }
}