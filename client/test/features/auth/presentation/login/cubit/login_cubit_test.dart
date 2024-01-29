import 'package:bloc_test/bloc_test.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_access_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:dartz/dartz.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';

class MockPostLoginUseCase extends Mock implements PostLoginUseCase {}

class MockSaveAccessTokenUseCase extends Mock
    implements SaveAccessTokenUseCase {}

class MockSaveRefreshTokenUseCase extends Mock
    implements SaveRefreshTokenUseCase {}

void main() {
  late LoginCubit loginCubit;
  late MockPostLoginUseCase mockPostLoginUseCase;
  late MockSaveAccessTokenUseCase mockSaveAccessTokenUseCase;
  late MockSaveRefreshTokenUseCase mockSaveRefreshTokenUseCase;

  setUpAll(() {
    registerFallbackValue(LoginParams(
        email: 'test@mail.com',
        password: '12345678')); // Provide a dummy instance of LoginParams
  });

  setUp(() {
    mockPostLoginUseCase = MockPostLoginUseCase();
    mockSaveAccessTokenUseCase = MockSaveAccessTokenUseCase();
    mockSaveRefreshTokenUseCase = MockSaveRefreshTokenUseCase();

    loginCubit = LoginCubit(
      mockPostLoginUseCase,
      mockSaveAccessTokenUseCase,
      mockSaveRefreshTokenUseCase,
    );
  });

  tearDown(() {
    loginCubit.close();
  });

  group('LoginCubit', () {
    const email = 'test@example.com';
    const password = 'password';

    test('initial state is LoginState.initial', () {
      expect(loginCubit.state, equals(const LoginState.initial()));
    });

    blocTest<LoginCubit, LoginState>(
      'emits [Loading, Success] when login is successful',
      build: () {
        when(() => mockPostLoginUseCase.call(any())).thenAnswer((_) async =>
            const Right(LoginResponseEntity(
                accessToken: 'token', refreshToken: 'refresh')));

        when(() => mockSaveAccessTokenUseCase.call(any()))
            .thenAnswer((_) async => const Right(null));

        when(() => mockSaveRefreshTokenUseCase.call(any()))
            .thenAnswer((_) async => const Right(null));

        return loginCubit;
      },
      act: (cubit) => cubit.login(email: email, password: password),
      expect: () => [
        const LoginState.loading(),
        const LoginState.success(
            LoginResponseEntity(accessToken: 'token', refreshToken: 'refresh')),
      ],
    );

    blocTest<LoginCubit, LoginState>(
      'emits [Loading, Failure] when login fails',
      build: () {
        when(() => mockPostLoginUseCase.call(any())).thenAnswer(
            (_) async => const Left(ServerFailure('Error message')));
        return loginCubit;
      },
      act: (cubit) => cubit.login(email: email, password: password),
      expect: () => [
        const LoginState.loading(),
        const LoginState.failure('Error message'),
      ],
    );
  });
}
