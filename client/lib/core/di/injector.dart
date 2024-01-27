import 'package:cointrade/features/auth/data/datasources/auth_local_datasource.dart';
import 'package:cointrade/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:cointrade/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:cointrade/features/auth/domain/usecase/get_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_reset_password_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_access_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/features/auth/presentation/reset_password/cubit/reset_password_cubit.dart';
import 'package:cointrade/features/bots/data/datasources/bot_remote_datasource.dart';
import 'package:cointrade/features/bots/data/repositories/bot_repository_impl.dart';
import 'package:cointrade/features/bots/domain/repositories/bot_repository.dart';
import 'package:cointrade/features/bots/domain/usecases/create_bot_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/delete_bot_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bot_buy_orders_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bot_sell_orders_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_bots_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/fetch_predictions_use_case.dart';
import 'package:cointrade/features/bots/domain/usecases/toggle_bot_enabled_use_case.dart';
import 'package:cointrade/features/bots/presentation/add_bot/cubit/add_bot_cubit.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_sell_orders/cubit/bot_sell_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/predictions/cubit/predictions_cubit.dart';
import 'package:cointrade/features/wallet/data/datasources/wallet_remote_datasouce.dart';
import 'package:cointrade/features/wallet/data/repositories/wallet_repository_impl.dart';
import 'package:cointrade/features/wallet/domain/repositories/wallet_repository.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_all_cryptocurrencies_use_case.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_current_price_for_trading_pair_use_case.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_historical_prices_use_case.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_total_balance_use_case.dart';
import 'package:cointrade/features/wallet/domain/usecases/fetch_wallet_use_case.dart';
import 'package:cointrade/features/wallet/domain/usecases/make_order_use_case.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/cubit/all_cryptocurrencies_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/converter_cubit/buy_sell_trading_pair_price_converter_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/current_price_trading_pair_cubit/current_price_trading_pair_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/make_order_cubit/make_order_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/total_balance/cubit/total_balance_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/wallet/cubit/wallet_cubit.dart';
import 'package:cointrade/features/settings/data/datasources/link_binance_remote_datasource.dart';
import 'package:cointrade/features/settings/data/repositories/link_binance_repository_impl.dart';
import 'package:cointrade/features/settings/domain/repositories/link_binance_repository.dart';
import 'package:cointrade/features/settings/domain/usecases/post_link_binance_use_case.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/cubit/connect_binance_cubit.dart';
import 'package:get_it/get_it.dart';

import '../../features/auth/domain/usecase/post_register_use_case.dart';

final sl = GetIt.instance;

Future<void> initDependencies() async {
  sl.registerSingleton<DioClient>(DioClient());
  registerDataSources();
  registerRepositories();
  registerUseCases();
  registerBlocs();
}

void registerBlocs() {
  sl.registerFactory(() => RegisterCubit(sl()));
  sl.registerFactory(() => LoginCubit(sl(), sl(), sl()));
  sl.registerFactory(() => ResetPasswordCubit(sl()));
  sl.registerFactory(() => ConnectBinanceCubit(sl()));
  sl.registerFactory(() => TotalBalanceCubit(sl()));
  sl.registerFactory(() => WalletCubit(sl()));
  sl.registerFactory(() => AllCryptocurrenciesCubit(sl()));
  sl.registerFactory(() => HistoricalPricesCubit(sl()));
  sl.registerFactory(() => BuySellTradingPairPriceConverterCubit());
  sl.registerFactory(() => CurrentPriceTradingPairCubit(sl()));
  sl.registerFactory(() => MakeOrderCubit(sl()));
  sl.registerFactory(() => AddBotCubit(sl()));
  sl.registerFactory(() => BotsCubit(sl(),sl(),sl()));
  sl.registerFactory(() => BotBuyOrdersCubit(sl()));
  sl.registerFactory(() => BotSellOrdersCubit(sl()));
  sl.registerFactory(() => PredictionsCubit(sl()));
}

void registerUseCases() {
  sl.registerLazySingleton(() => PostLoginUseCase(sl()));
  sl.registerLazySingleton(() => PostRegisterUseCase(sl()));
  sl.registerLazySingleton(() => PostResetPasswordUseCase(sl()));
  sl.registerLazySingleton(() => PostLinkBinanceUseCase(sl()));
  sl.registerLazySingleton(() => SaveAccessTokenUseCase(sl()));
  sl.registerLazySingleton(() => SaveRefreshTokenUseCase(sl()));
  sl.registerLazySingleton(() => GetRefreshTokenUseCase(sl()));
  sl.registerLazySingleton(() => PostRefreshTokenUseCase(sl()));
  sl.registerLazySingleton(() => FetchTotalBalanceUseCase(sl()));
  sl.registerLazySingleton(() => FetchWalletUseCase(sl()));
  sl.registerLazySingleton(() => FetchAllCryptocurrenciesUseCase(sl()));
  sl.registerLazySingleton(() => FetchHistoricalPricesUseCase(sl()));
  sl.registerLazySingleton(() => FetchCurrentPriceForTradingPairUseCase(sl()));
  sl.registerLazySingleton(() => MakeOrderUseCase(sl()));
  sl.registerLazySingleton(() => CreateBotUseCase(sl()));
  sl.registerLazySingleton(() => FetchBotsUseCase(sl()));
  sl.registerLazySingleton(() => ToggleBotEnabledUseCase(sl()));
  sl.registerLazySingleton(() => DeleteBotUseCase(sl()));
  sl.registerLazySingleton(() => FetchBotBuyOrdersUseCase(sl()));
  sl.registerLazySingleton(() => FetchBotSellOrdersUseCase(sl()));
  sl.registerLazySingleton(() => FetchPredictionsUseCase(sl()));
}

void registerRepositories() {
  sl.registerLazySingleton<AuthRepository>(
      () => AuthRepositoryImpl(sl(), sl()));
  sl.registerLazySingleton<LinkBinanceRepository>(
      () => LinkBinanceRepositoryImpl(sl()));
  sl.registerLazySingleton<WalletRepository>(() => WalletRepositoryImpl(sl()));
  sl.registerLazySingleton<BotRepository>(() => BotRepositoryImpl(sl()));
}

void registerDataSources() {
  sl.registerLazySingleton<AuthRemoteDataSource>(
      () => AuthRemoteDataSourceImpl(sl()));
  sl.registerLazySingleton(() => AuthLocalDataSource());
  sl.registerLazySingleton(() => LinkBinanceRemoteDataSource(sl()));
  sl.registerLazySingleton(() => WalletRemoteDataSource(sl()));
  sl.registerLazySingleton(() => BotRemoteDataSource(sl()));
}
