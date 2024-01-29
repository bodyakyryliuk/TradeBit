import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/predictions_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class FetchPredictionsUseCase
    implements UseCase<PredictionsResponseModel, PredictionsParams> {
  final BotRepository botRepository;

  FetchPredictionsUseCase(this.botRepository);

  @override
  Future<Either<Failure, PredictionsResponseModel>> call(
      PredictionsParams params) async {
    return await botRepository.fetchPredictions(params);
  }
}
