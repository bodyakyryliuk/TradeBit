import 'package:bloc/bloc.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_historical_prices_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'historical_prices_state.dart';

part 'historical_prices_cubit.freezed.dart';

class HistoricalPricesCubit extends Cubit<HistoricalPricesState> {
  HistoricalPricesCubit(this._fetchHistoricalPricesUseCase)
      : super(const HistoricalPricesState.initial());

  final FetchHistoricalPricesUseCase _fetchHistoricalPricesUseCase;

  Future<void> fetchHistoricalPrices(
      {required String currencyPair, required int period}) async {
    emit(const HistoricalPricesState.loading());
    final data = await _fetchHistoricalPricesUseCase.call(
        HistoricalPricesParams(currencyPair: currencyPair, period: period));

    data.fold(
      (Failure failure) {
        emit(HistoricalPricesState.failure(failure.message!));
      },
      (historicalPricesResponseModel) {
        emit(HistoricalPricesState.success(historicalPricesResponseModel));
      },
    );
  }
}
