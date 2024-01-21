import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/home/data/models/wallet_response_model.dart';
import 'package:cointrade/features/home/domain/usecases/fetch_wallet_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'wallet_state.dart';

part 'wallet_cubit.freezed.dart';

class WalletCubit extends Cubit<WalletState> {
  final FetchWalletUseCase fetchWalletUseCase;

  WalletCubit(this.fetchWalletUseCase) : super(const WalletState.initial());

  Future<void> fetchWallet() async {
    emit(const WalletState.loading());
    final data = await fetchWalletUseCase.call(WalletParams(
        apiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceApiKey),
        secretApiKey: HiveBoxes.appStorageBox.get(DbKeys.binanceSecretApiKey)));
    data.fold(
      (failure) {
        emit(WalletState.failure(failure.message!));
      },
      (walletResponseModel) {
        emit(WalletState.success(walletResponseModel));
      },
    );
  }
}
