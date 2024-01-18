import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:cointrade/features/settings/domain/usecases/post_link_binance_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'connect_binance_state.dart';

part 'connect_binance_cubit.freezed.dart';

class ConnectBinanceCubit extends Cubit<ConnectBinanceState> {
  ConnectBinanceCubit(this.postLinkBinanceUseCase) : super(const ConnectBinanceState.initial());

  final PostLinkBinanceUseCase postLinkBinanceUseCase;

  Future<void> connectBinance({
    required String apiKey,
    required String secretApiKey,
  }) async {
    emit(const ConnectBinanceState.loading());
    final data = await postLinkBinanceUseCase.call(LinkBinanceParams(
      apiKey: apiKey,
      secretApiKey: secretApiKey,
    ));
    data.fold(
      (failure) {
        emit(ConnectBinanceState.failure(failure.message!));
      },
      (linkBinanceResponse) {
        //todo usecase HiveBoxes.appStorageBox.put(DbKeys.accessTokenKey, loginResponse.token);
        emit(ConnectBinanceState.success(linkBinanceResponse));
      },
    );
  }
}
