import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_access_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_refresh_token_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'login_state.dart';

part 'login_cubit.freezed.dart';

class LoginCubit extends Cubit<LoginState> {
  LoginCubit(this.postLogin, this.saveAccessToken, this.saveRefreshToken) : super(const LoginState.initial());
  final PostLoginUseCase postLogin;
  final SaveAccessTokenUseCase saveAccessToken ;
  final SaveRefreshTokenUseCase saveRefreshToken;


  Future<void> login({
    required String email,
    required String password,
  }) async{
    emit(const LoginState.loading());
    final data = await postLogin.call(LoginParams(
      email: email,
      password: password,
    ));
    data.fold(
      (failure) {
        emit(LoginState.failure(failure.message!));
      },
      (loginResponse) {
        saveAccessToken.call(loginResponse.accessToken);
        saveRefreshToken.call(loginResponse.refreshToken);
        emit(LoginState.success(loginResponse));
      },
    );
  }
}
