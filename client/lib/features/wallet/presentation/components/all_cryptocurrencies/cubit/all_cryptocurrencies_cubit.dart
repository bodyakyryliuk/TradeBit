import 'package:bloc/bloc.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_all_cryptocurrencies_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'all_cryptocurrencies_state.dart';

part 'all_cryptocurrencies_cubit.freezed.dart';

class AllCryptocurrenciesCubit extends Cubit<AllCryptocurrenciesState> {
  AllCryptocurrenciesCubit(this.fetchAllCryptocurrenciesUseCase)
      : super(const AllCryptocurrenciesState.initial());

  final FetchAllCryptocurrenciesUseCase fetchAllCryptocurrenciesUseCase;

  Future<void> fetchAllCryptocurrencies() async {
    emit(const AllCryptocurrenciesState.loading());
    final data = await fetchAllCryptocurrenciesUseCase.call(NoParams());
    data.fold(
      (failure) {
        emit(AllCryptocurrenciesState.failure(failure.message!));
      },
      (allCryptocurrenciesResponseModel) {
        emit(
            AllCryptocurrenciesState.success(allCryptocurrenciesResponseModel));
      },
    );
  }
}
