import 'package:bloc/bloc.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bots_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'bots_state.dart';

class BotsCubit extends Cubit<BotsState> {
  final FetchBotsUseCase fetchBotsUseCase;

  BotsCubit(this.fetchBotsUseCase) : super(BotsState(bots: [], errorMessage: null));

  List<BotModel> bots = [];

  Future<void> fetchBots() async {
    emit(BotsState(bots: bots, requested: true));
    final response = await fetchBotsUseCase.call(NoParams());
    response.fold(
      (failure) => emit(
          BotsState(bots: bots, errorMessage: failure.message!, requested: false)),
      (botsResponseModel) {
        bots = botsResponseModel.bots!;
        emit(BotsState(
            bots: botsResponseModel.bots!, requested: false, errorMessage: null));
      },
    );
  }
}
