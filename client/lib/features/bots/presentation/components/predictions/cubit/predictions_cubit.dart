import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/bots/data/models/predictions_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_predictions_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'predictions_state.dart';

part 'predictions_cubit.freezed.dart';

class PredictionsCubit extends Cubit<PredictionsState> {
  PredictionsCubit(this._fetchPredictionsUseCase)
      : super(const PredictionsState.initial());

  final FetchPredictionsUseCase _fetchPredictionsUseCase;

  Future<void> fetchPredictions(String tradingPair) async {
    emit(const PredictionsState.loading());
    final response = await _fetchPredictionsUseCase(
        PredictionsParams(tradingPair: tradingPair));
    response.fold(
      (failure) => emit(PredictionsState.failure(failure.message!)),
      (predictionsResponseModel) =>
          emit(PredictionsState.success(predictionsResponseModel)),
    );
  }
}
