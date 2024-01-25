import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/toggle_bot_enabled_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class ToggleBotEnabledUseCase
    extends UseCase<bool, ToggleBotEnabledParams> {
  final BotRepository _botRepository;

  ToggleBotEnabledUseCase(this._botRepository);

  @override
  Future<Either<Failure, bool>> call(
      ToggleBotEnabledParams params) async {
    return await _botRepository.toggleBotEnabled(params);
  }
}
