import 'package:bloc/bloc.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_reset_password_use_case.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'reset_password_state.dart';

part 'reset_password_cubit.freezed.dart';

class ResetPasswordCubit extends Cubit<ResetPasswordState> {
  ResetPasswordCubit(this.postResetPassword)
      : super(const ResetPasswordState.initial());
  final PostResetPasswordUseCase postResetPassword;

  Future<void> resetPassword({required String email}) async {
    emit(const ResetPasswordState.loading());
    final data =
        await postResetPassword.call(ResetPasswordParams(email: email));
    data.fold(
      (failure) {
        emit(ResetPasswordState.failure(failure.message!));
      },
      (resetPasswordResponse) {
        emit(ResetPasswordState.success(resetPasswordResponse));
      },
    );
  }
}
