import 'package:equatable/equatable.dart';

class LoginResponseEntity extends Equatable{
  final String? accessToken;
  final String? refreshToken;
  const LoginResponseEntity({ this.accessToken, this.refreshToken});

  @override
  List<Object?> get props => [accessToken];
}