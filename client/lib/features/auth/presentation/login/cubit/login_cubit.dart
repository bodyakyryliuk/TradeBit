import 'package:bloc/bloc.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'login_state.dart';

part 'login_cubit.freezed.dart';

class LoginCubit extends Cubit<LoginState> {
  LoginCubit(this.postLogin) : super(const LoginState.initial());
  final PostLoginUseCase postLogin;


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
        HiveBoxes.appStorageBox.put(DbKeys.accessTokenKey, loginResponse.token);
        emit(LoginState.success(loginResponse));
      },
    );
  }
}
