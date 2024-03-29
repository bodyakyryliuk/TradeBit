import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/delete_bot_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bots_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/toggle_bot_enabled_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'bots_state.dart';

class BotsCubit extends Cubit<BotsState> {
  final FetchBotsUseCase fetchBotsUseCase;
  final ToggleBotEnabledUseCase toggleBotEnabledUseCase;
  final DeleteBotUseCase deleteBotUseCase;

  BotsCubit(this.fetchBotsUseCase, this.toggleBotEnabledUseCase,
      this.deleteBotUseCase)
      : super(BotsState(bots: [], errorMessage: null));


  List<BotModel> bots = [];

  Future<void> fetchBots() async {
    emit(BotsState(bots: bots, requested: true));
    final response = await fetchBotsUseCase.call(NoParams());
    response.fold(
      (failure) => emit(BotsState(
          bots: bots, errorMessage: failure.message!, requested: false)),
      (botsResponseModel) {
        bots = botsResponseModel.bots!;
        emit(BotsState(
            bots: botsResponseModel.bots!,
            requested: false,
            errorMessage: null));
      },
    );
  }

  Future<void> toggleBotEnabled(int botId) async {
    emit(BotsState(bots: bots, requested: true));
    final response = await toggleBotEnabledUseCase
        .call(ToggleBotEnabledParams(botId: botId));
    response.fold(
      (failure) => emit(BotsState(
          bots: bots, errorMessage: failure.message!, requested: false)),
      (toggleBotEnabledResponseModel) {
        fetchBots();
      },
    );
  }

  Future<void> deleteBot(int botId) async {
    emit(BotsState(
        bots: bots..removeWhere((element) => element.id == botId),
        requested: true));
    final response = await deleteBotUseCase.call(DeleteBotParams(botId: botId));
    response.fold(
      (failure) => emit(BotsState(
          bots: bots, errorMessage: failure.message!, requested: false)),
      (toggleBotEnabledResponseModel) {
        fetchBots();
      },
    );
  }
}
