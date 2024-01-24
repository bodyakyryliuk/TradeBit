import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/create_bot_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/create_bot_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'add_bot_state.dart';

part 'add_bot_cubit.freezed.dart';

class AddBotCubit extends Cubit<AddBotState> {
  AddBotCubit(this._createBotUseCase) : super(const AddBotState.initial());

  final CreateBotUseCase _createBotUseCase;

  Future<void> createBot(
      {required String name,
      required double buyThreshold,
      required double sellThreshold,
      required double takeProfitPercentage,
      required double stopLossPercentage,
      required double tradeSize,
      required String tradingPair}) async {
    emit(const AddBotState.loading());
    final result = await _createBotUseCase(CreateBotParams(
      name: name,
      buyThreshold: buyThreshold,
      sellThreshold: sellThreshold,
      takeProfitPercentage: takeProfitPercentage,
      stopLossPercentage: stopLossPercentage,
      tradeSize: tradeSize,
      tradingPair: tradingPair,
    ));
    result.fold(
      (failure) => emit(AddBotState.failure(failure.message!)),
      (CreateBotResponseModel createBotResponseModel) =>
          emit(AddBotState.success(createBotResponseModel)),
    );
  }
}
