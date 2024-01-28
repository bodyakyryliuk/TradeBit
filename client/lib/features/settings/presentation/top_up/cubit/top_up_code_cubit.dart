import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/settings/data/models/top_up_code_response_model.dart';
import 'package:cointrade/features/settings/domain/usecases/fetch_top_up_code_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'top_up_code_state.dart';

part 'top_up_code_cubit.freezed.dart';

class TopUpCodeCubit extends Cubit<TopUpCodeState> {
  TopUpCodeCubit(this._fetchTopUpCodeUseCase)
      : super(const TopUpCodeState.initial());

  final FetchTopUpCodeUseCase _fetchTopUpCodeUseCase;

  Future<void> fetchTopUpCode({required String coin}) async {
    emit(const TopUpCodeState.loading());
    final response = await _fetchTopUpCodeUseCase(TopUpCodeParams(
      coin: coin,
      apiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceApiKey),
      secretApiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceSecretApiKey),
    ));
    response.fold(
      (failure) => emit(TopUpCodeState.failure(failure.message!)),
      (data) => emit(TopUpCodeState.success(data)),
    );
  }
}
