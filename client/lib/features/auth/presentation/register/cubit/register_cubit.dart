import 'package:bloc/bloc.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_register_use_case'
    '.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'register_state.dart';

part 'register_cubit.freezed.dart';

class RegisterCubit extends Cubit<RegisterState> {
  RegisterCubit(this.postRegister) : super(const RegisterState.initial());
  final PostRegisterUseCase postRegister;

  Future<void> register({
    required String firstName,
    required String lastName,
    required String email,
    required String password,
  }) async {
    emit(const RegisterState.loading());
    final data = await postRegister.call(RegisterParams(
      firstName: firstName,
      lastName: lastName,
      email: email,
      password: password,
    ));
    data.fold(
      (Failure failure) {
        emit(RegisterState.failure(failure.message!));
      },
      (registerResponse) {
        emit(RegisterState.success(registerResponse));
      },
    );
  }
}
