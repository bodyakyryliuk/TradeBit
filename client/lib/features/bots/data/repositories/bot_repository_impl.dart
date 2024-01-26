import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/datasources/bot_remote_datasource.dart';
import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';
import 'package:cointrade/features/bots/data/models/toggle_bot_enabled_response_model.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:dartz/dartz.dart';

class BotRepositoryImpl implements BotRepository {
  final BotRemoteDataSource botRemoteDataSource;

  BotRepositoryImpl(this.botRemoteDataSource);

  @override
  Future<Either<Failure, CreateBotResponseModel>> createBot(
      CreateBotParams createBotParams) async {
    try {
      final response = await botRemoteDataSource.createBot(createBotParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, BotsResponseModel>> fetchBots() async {
    try {
      final response = await botRemoteDataSource.fetchBots();
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, bool>> toggleBotEnabled(
      ToggleBotEnabledParams toggleBotEnabledParams) async {
    try {
      final response =
          await botRemoteDataSource.toggleBotEnabled(toggleBotEnabledParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, void>> deleteBot(DeleteBotParams deleteBotParams) async{
    try {
      final response =
      await botRemoteDataSource.deleteBot(deleteBotParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }

  @override
  Future<Either<Failure, BotBuyOrdersResponseModel>> fetchBotBuyOrders(BotBuyOrdersParams botBuyOrdersParams) async{
    try {
      final response =
          await botRemoteDataSource.fetchBotBuyOrders(botBuyOrdersParams);
      return Right(response);
    } on ServerException catch (e) {
      return Left(ServerFailure(e.message));
    } catch (e) {
      return const Left(ServerFailure('Unknown error occured'));
    }
  }
}
