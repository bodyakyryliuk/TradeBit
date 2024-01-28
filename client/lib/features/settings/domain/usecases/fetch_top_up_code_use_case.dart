import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/settings/data/models/top_up_code_response_model.dart';
import 'package:cointrade/features/settings/domain/repositories/link_binance_repository.dart';
import 'package:dartz/dartz.dart';

class FetchTopUpCodeUseCase implements UseCase<TopUpCodeResponseModel, TopUpCodeParams> {
  final LinkBinanceRepository repository;

  FetchTopUpCodeUseCase(this.repository);

  @override
  Future<Either<Failure, TopUpCodeResponseModel>> call(TopUpCodeParams params) async {
    return await repository.fetchTopUpCode(params);
  }
}