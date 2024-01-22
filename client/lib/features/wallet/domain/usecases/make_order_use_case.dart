import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/make_order_response_model.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:dartz/dartz.dart';

class MakeOrderUseCase implements UseCase<MakeOrderResponseModel, MakeOrderParams> {
  final WalletRepository walletRepository;

  MakeOrderUseCase(this.walletRepository);

  @override
  Future<Either<Failure, MakeOrderResponseModel>> call(MakeOrderParams params) async {
    return await walletRepository.makeOrder(params);
  }
}