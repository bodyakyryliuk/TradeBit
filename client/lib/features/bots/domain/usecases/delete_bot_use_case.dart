import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class DeleteBotUseCase implements UseCase<void, DeleteBotParams>{
  final BotRepository botRepository;

  DeleteBotUseCase(this.botRepository);

  @override
  Future<Either<Failure, void>> call(DeleteBotParams params) async {
    return await botRepository.deleteBot(params);
  }
}