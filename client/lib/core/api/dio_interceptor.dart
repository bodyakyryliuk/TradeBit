import 'dart:convert';

import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/di/injector.dart';
import 'package:cointrade/core/params/params.dart';
import 'package:cointrade/core/usecase/usecase.dart';
import 'package:cointrade/core/utils/logger.dart';
import 'package:cointrade/features/auth/data/models/refresh_token_response_model.dart';
import 'package:cointrade/features/auth/domain/usecase/get_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/post_refresh_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_access_token_use_case.dart';
import 'package:cointrade/features/auth/domain/usecase/save_refresh_token_use_case.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

class DioLogInterceptor extends Interceptor {
  String? accessToken;
  final Dio dio;

  DioLogInterceptor({required this.accessToken, required this.dio});

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {


    String headerMessage = "";
    options.headers
        .forEach((key, value) => headerMessage += '► $key: $value\n');
    try {
      options.queryParameters.forEach(
        (key, value) => debugPrint('► $key: $value\n'),
      );
    } catch (_) {}

    final queryParameters = options.queryParameters.entries
        .map((entry) => '► ${entry.key}: ${entry.value}\n')
        .join();
    try {
      const JsonEncoder encoder = JsonEncoder.withIndent(" ");
      final String prettyJson = encoder.convert(options.data);
      log.d(
        // ignore: unnecessary_null_comparison
        "REQUEST ► ︎ ${options.method != null ? options.method.toUpperCase() : 'METHOD'} ${"${options.baseUrl}${options.path}"}\n\n"
        "Headers:\n"
        "$headerMessage\n"
        "❖ QueryParameters : $queryParameters\n"
        "Body: $prettyJson",
      );
    } catch (e) {
      log.e("Failed to extract json request $e");
    }
    super.onRequest(options, handler);
  }

  @override
  Future<void> onError(
      DioException err, ErrorInterceptorHandler handler) async {
    log.e(
      "<-- ${err.message} ${err.response?.requestOptions != null ? (err.response!.requestOptions.baseUrl + err.response!.requestOptions.path) : 'URL'}\n\n"
      "${err.response != null ? err.response!.data : 'Unknown Error'}",
    );

    if (err.response?.statusCode == 401) {
      // If a 401 response is received, refresh the access token
      String? newAccessToken = await refreshToken();
      this.accessToken = newAccessToken;

      if (newAccessToken != null) {
        // Update the request header with the new access token
        err.requestOptions.headers['Authorization'] = 'Bearer $newAccessToken';

        log.i('Old token: $accessToken\n new token: $newAccessToken');
        log.i(accessToken == newAccessToken);

        // Repeat the request with the updated header
        return handler.resolve(await dio.fetch(err.requestOptions));
      }
    }

    super.onError(err, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    String headerMessage = "";
    response.headers.forEach((k, v) => headerMessage += '► $k: $v\n');

    const JsonEncoder encoder = JsonEncoder.withIndent(" ");
    final String prettyJson = encoder.convert(response.data);
    log.d(
      // ignore: unnecessary_null_comparison
      "◀ ︎RESPONSE ${response.statusCode} ${response.requestOptions != null ? (response.requestOptions.baseUrl + response.requestOptions.path) : 'URL'}\n\n"
      "Headers:\n"
      "$headerMessage\n"
      "❖ Results : \n"
      "Response: $prettyJson",
    );
    super.onResponse(response, handler);
  }

  Future<String?> refreshToken() async {
    var currRefreshTokenResult =
        await sl<GetRefreshTokenUseCase>().call(NoParams());
    String? currRefreshToken;

    currRefreshTokenResult.fold(
      (failure) {
        currRefreshToken = null;
      },
      (rToken) => currRefreshToken = rToken,
    );

    if (currRefreshToken == null) return null;

    var postRefreshTokenResult = await sl<PostRefreshTokenUseCase>()
        .call(RefreshTokenParams(refreshToken: currRefreshToken!));

    String? newAccessToken;
    RefreshTokenResponseModel? refreshTokenResponseModel;

    postRefreshTokenResult.fold(
      (failure) {
        HiveBoxes.appStorageBox.delete(DbKeys.accessTokenKey);
      },
      (responseModel) {
        refreshTokenResponseModel =
            responseModel; // Assign to outer scope variable
        newAccessToken = responseModel.accessToken;
      },
    );

    if (refreshTokenResponseModel != null) {
      await sl<SaveAccessTokenUseCase>()
          .call(refreshTokenResponseModel!.accessToken);
      await sl<SaveRefreshTokenUseCase>()
          .call(refreshTokenResponseModel!.refreshToken);
    }

    return newAccessToken;
  }
}
