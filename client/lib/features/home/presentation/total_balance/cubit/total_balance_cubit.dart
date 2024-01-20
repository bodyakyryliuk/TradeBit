import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/home/data/models/total_balance_response_model.dart';
import 'package:cointrade/features/home/domain/usecases/fetch_total_balance_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'total_balance_state.dart';

part 'total_balance_cubit.freezed.dart';

class TotalBalanceCubit extends Cubit<TotalBalanceState> {
  final FetchTotalBalanceUseCase fetchTotalBalanceUseCase;

  TotalBalanceCubit(this.fetchTotalBalanceUseCase)
      : super(const TotalBalanceState.initial());

  Future<void> fetchTotalBalance() async {
    emit(const TotalBalanceState.loading());
    final data = await fetchTotalBalanceUseCase.call(TotalBalanceParams(
        apiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceApiKey),
        secretApiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceSecretApiKey)));
    data.fold(
      (failure) {
        emit(TotalBalanceState.failure(failure.message!));
      },
      (totalBalanceResponseModel) {
        emit(TotalBalanceState.success(totalBalanceResponseModel));
      },
    );
  }
}
