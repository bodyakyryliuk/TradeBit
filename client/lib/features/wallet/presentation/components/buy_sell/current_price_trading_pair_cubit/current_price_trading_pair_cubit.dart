import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/wallet/data/models/current_price_for_trading_pair_response_model.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_current_price_for_trading_pair_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'current_price_trading_pair_state.dart';

part 'current_price_trading_pair_cubit.freezed.dart';

class CurrentPriceTradingPairCubit extends Cubit<CurrentPriceTradingPairState> {
  final FetchCurrentPriceForTradingPairUseCase
      _fetchCurrentPriceForTradingPairUseCase;

  CurrentPriceTradingPairCubit(this._fetchCurrentPriceForTradingPairUseCase)
      : super(const CurrentPriceTradingPairState.initial());

  Future<void> fetchCurrentPriceForTradingPair({required String tradingPair}) async {
    emit(const CurrentPriceTradingPairState.loading());
    final data = await _fetchCurrentPriceForTradingPairUseCase
        .call(CurrentPriceForTradingPairParams(tradingPair: tradingPair));
    data.fold(
      (failure) {
        emit(CurrentPriceTradingPairState.failure(failure.message!));
      },
      (currentPriceForTradingPairResponseModel) {
        emit(CurrentPriceTradingPairState.success(
            currentPriceForTradingPairResponseModel));
      },
    );
  }
}
