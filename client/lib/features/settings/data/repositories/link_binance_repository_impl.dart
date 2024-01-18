import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/settings/data/datasources/link_binance_remote_datasource.dart';
import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:cointrade/features/settings/domain/repositories/link_binance_repository.dart';
import 'package:dartz/dartz.dart';

class LinkBinanceRepositoryImpl implements LinkBinanceRepository {
  final LinkBinanceRemoteDataSource linkBinanceRemoteDataSource;

  LinkBinanceRepositoryImpl(this.linkBinanceRemoteDataSource);

  @override
  Future<Either<Failure, LinkBinanceResponseEntity>> linkBinance(
      LinkBinanceParams linkBinanceParams) async {
    try {
      final response =
          await linkBinanceRemoteDataSource.linkBinance(linkBinanceParams);
      return Right(response.toEntity());
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }
}
