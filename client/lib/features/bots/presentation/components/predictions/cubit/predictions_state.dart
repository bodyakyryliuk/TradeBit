part of 'predictions_cubit.dart';

@freezed
class PredictionsState with _$PredictionsState {
  const factory PredictionsState.initial() = _Initial;
  const factory PredictionsState.loading() = _Loading;
  const factory PredictionsState.success(PredictionsResponseModel predictionsResponseModel) = _Success;
  const factory PredictionsState.failure(String message) = _Failure;
}
