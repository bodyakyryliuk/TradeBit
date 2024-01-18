import 'package:cointrade/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:cointrade/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_reset_password_use_case.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/features/auth/presentation/reset_password/cubit/reset_password_cubit.dart';
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
  sl.registerFactory(() => LoginCubit(sl()));
  sl.registerFactory(() => ResetPasswordCubit(sl()));
  sl.registerFactory(() => ConnectBinanceCubit(sl()));
}

void registerUseCases() {
  sl.registerLazySingleton(() => PostLoginUseCase(sl()));
  sl.registerLazySingleton(() => PostRegisterUseCase(sl()));
  sl.registerLazySingleton(() => PostResetPasswordUseCase(sl()));
  sl.registerLazySingleton(() => PostLinkBinanceUseCase(sl()));
}

void registerRepositories() {
  sl.registerLazySingleton<AuthRepository>(() => AuthRepositoryImpl(sl()));
  sl.registerLazySingleton<LinkBinanceRepository>(() => LinkBinanceRepositoryImpl(sl()));
}

void registerDataSources() {
  sl.registerLazySingleton<AuthRemoteDataSource>(
      () => AuthRemoteDataSourceImpl(sl()));
  sl.registerLazySingleton(
      () => LinkBinanceRemoteDataSource(sl()));
}
