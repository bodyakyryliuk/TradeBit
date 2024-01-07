import 'package:cointrade/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:cointrade/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:cointrade/features/auth/domain/repositories/auth_repository.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:cointrade/core/api/dio_client.dart';
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
}

void registerUseCases() {
  sl.registerLazySingleton(() => PostLoginUseCase(sl()));
  sl.registerLazySingleton(() => PostRegisterUseCase(sl()));
}

void registerRepositories() {
  sl.registerLazySingleton<AuthRepository>(() => AuthRepositoryImpl(sl()));
}

void registerDataSources() {
  sl.registerLazySingleton<AuthRemoteDataSource>(
      () => AuthRemoteDataSourceImpl(sl()));
}
