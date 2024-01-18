import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:dartz/dartz.dart';

abstract class LinkBinanceRepository{
  Future<Either<Failure, LinkBinanceResponseEntity>> linkBinance(
      LinkBinanceParams linkBinanceParams);
}