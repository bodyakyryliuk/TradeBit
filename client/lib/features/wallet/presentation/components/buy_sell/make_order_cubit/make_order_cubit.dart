import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/wallet/data/models/make_order_response_model.dart';
import 'package:cointrade/features/wallet/domain/usecases/make_order_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'make_order_state.dart';

part 'make_order_cubit.freezed.dart';

enum OrderSide { buy, sell }

class MakeOrderCubit extends Cubit<MakeOrderState> {
  final MakeOrderUseCase makeOrderUseCase;

  MakeOrderCubit(this.makeOrderUseCase) : super(const MakeOrderState.initial());

  Future<void> makeOrder(
      {required OrderSide orderSide,
      required String quantity,
      required String symbol}) async {
    emit(const MakeOrderState.loading());
    final response = await makeOrderUseCase(MakeOrderParams(
      orderDto: OrderDto(
        type: 'MARKET',
        quantity: double.parse(quantity).toStringAsFixed(4),
        side: orderSide.name.toUpperCase(),
        symbol: symbol,
      ),
      linkDto: LinkDto(
        apiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceApiKey),
        secretApiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceSecretApiKey),
      ),
    ));
    response.fold(
      (failure) => emit(MakeOrderState.failure(failure.message!)),
      (makeOrderResponseModel) =>
          emit(MakeOrderState.success(makeOrderResponseModel)),
    );
  }
}
