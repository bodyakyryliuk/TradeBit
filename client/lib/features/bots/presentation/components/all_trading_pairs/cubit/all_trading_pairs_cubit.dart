import 'package:bloc/bloc.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/bots/data/models/trading_pairs_response_model.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_trading_pairs_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'all_trading_pairs_state.dart';

part 'all_trading_pairs_cubit.freezed.dart';

class AllTradingPairsCubit extends Cubit<AllTradingPairsState> {
  AllTradingPairsCubit(this._fetchTradingPairsUseCase)
      : super(const AllTradingPairsState.initial());

  final FetchTradingPairsUseCase _fetchTradingPairsUseCase;

  Future<void> fetchTradingPairs() async {
    emit(const AllTradingPairsState.loading());
    final result = await _fetchTradingPairsUseCase(NoParams());
    result.fold(
      (failure) => emit(AllTradingPairsState.failure(failure.message!)),
      (tradingPairsResponseModel) =>
          emit(AllTradingPairsState.success(tradingPairsResponseModel)),
    );
  }
}
