part of 'all_cryptocurrencies_cubit.dart';

@freezed
class AllCryptocurrenciesState with _$AllCryptocurrenciesState {
  const factory AllCryptocurrenciesState.initial() = _Initial;

  const factory AllCryptocurrenciesState.loading() = _Loading;

  const factory AllCryptocurrenciesState.success(
          AllCryptocurrenciesResponseModel allCryptocurrenciesResponseModel) =
      _Success;

  const factory AllCryptocurrenciesState.failure(String message) = _Failure;
}
