import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bot_buy_orders_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'bot_buy_orders_state.dart';

part 'bot_buy_orders_cubit.freezed.dart';

class BotBuyOrdersCubit extends Cubit<BotBuyOrdersState> {
  BotBuyOrdersCubit(this._fetchBotBuyOrdersUseCase)
      : super(const BotBuyOrdersState.initial());

  final FetchBotBuyOrdersUseCase _fetchBotBuyOrdersUseCase;

  Future<void> fetchBotBuyOrders(int botId) async {
    emit(const BotBuyOrdersState.loading());
    final response = await _fetchBotBuyOrdersUseCase(BotBuyOrdersParams(botId:botId));
    response.fold(
      (failure) => emit(BotBuyOrdersState.failure(failure.message!)),
      (botBuyOrdersResponseModel) => emit(BotBuyOrdersState.success(botBuyOrdersResponseModel)),
    );
  }
}
