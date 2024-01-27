import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:cointrade/features/bots/data/models/bot_sell_orders_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bot_buy_orders_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bot_sell_orders_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'bot_sell_orders_state.dart';

part 'bot_sell_orders_cubit.freezed.dart';

class BotSellOrdersCubit extends Cubit<BotSellOrdersState> {
  BotSellOrdersCubit(this._fetchBotSellOrdersUseCase)
      : super(const BotSellOrdersState.initial());

  final FetchBotSellOrdersUseCase _fetchBotSellOrdersUseCase;

  Future<void> fetchBotSellOrders(int botId) async {
    emit(const BotSellOrdersState.loading());
    final response =
        await _fetchBotSellOrdersUseCase(BotSellOrdersParams(botId: botId));
    response.fold(
      (failure) => emit(BotSellOrdersState.failure(failure.message!)),
      (botSellOrdersResponseModel) =>
          emit(BotSellOrdersState.success(botSellOrdersResponseModel)),
    );
  }
}
