import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:cointrade/features/settings/domain/repositories/link_binance_repository.dart';
import 'package:dartz/dartz.dart';

class PostLinkBinanceUseCase
    extends UseCase<LinkBinanceResponseEntity, LinkBinanceParams> {
  final LinkBinanceRepository _repository;

  PostLinkBinanceUseCase(this._repository);

  @override
  Future<Either<Failure, LinkBinanceResponseEntity>> call(
          LinkBinanceParams params) =>
      _repository.linkBinance(params);
}
