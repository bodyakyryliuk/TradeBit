import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/error/exceptions.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/features/auth/data/models/login_response_model.dart';
import 'package:cointrade/features/auth/data/models/register_response_model.dart';
import 'package:cointrade/features/auth/data/models/reset_password_response_model.dart';
import 'package:cointrade/features/auth/domain/usecase/post_login_use_case.dart';
import 'package:cointrade/core/api/dio_client.dart';
import 'package:cointrade/features/auth/domain/usecase/post_register_use_case.dart';

abstract class AuthRemoteDataSource {
  Future<RegisterResponseModel> register(RegisterParams registerParams);
  Future<LoginResponse> login(LoginParams loginParams);
  Future<ResetPasswordResponseModel> resetPassword(ResetPasswordParams loginParams);
}

class AuthRemoteDataSourceImpl implements AuthRemoteDataSource {
  final DioClient _client;

  AuthRemoteDataSourceImpl(DioClient client) : _client = client;

  @override
  Future<LoginResponse> login(LoginParams loginParams) async {
    try {
      final response = await _client.postRequest(EndPoints.login,
          data: loginParams.toJson());
      final result = LoginResponse.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  @override
  Future<RegisterResponseModel> register(RegisterParams registerParams) async {
    try {
      final response = await _client.postRequest(EndPoints.register,
          data: registerParams.toJson());
      final result = RegisterResponseModel.fromJson(response.data);
      if (response.statusCode == 201) {
        return result;
      } else {
        throw ServerException(result.message);
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }

  @override
  Future<ResetPasswordResponseModel> resetPassword(ResetPasswordParams resetPasswordParams) async{
    try {
      final response = await _client.postRequest(EndPoints.resetPassword,
          data: resetPasswordParams.toJson());
      final result = ResetPasswordResponseModel.fromJson(response.data);
      if (response.statusCode == 200) {
        return result;
      } else {
        throw ServerException(result.message.toString());
      }
    } on ServerException catch (e) {
      throw ServerException(e.message);
    }
  }
}
