import 'dart:convert';

import 'package:cointrade/core/utils/logger.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

class DioInterceptor extends Interceptor {
  final String? accessToken;
  final Dio dio;

  DioInterceptor({required this.accessToken, required this.dio});

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    if (accessToken != null) {
      options.headers['Authorization'] = 'Bearer ${accessToken ?? ''}';
    }
    String headerMessage = "";
    options.headers
        .forEach((key, value) => headerMessage += '► $key: $value\n');
    try {
      options.queryParameters.forEach(
        (key, value) => debugPrint('► $key: $value\n'),
      );
    } catch (_) {}
    try {
      const JsonEncoder encoder = JsonEncoder.withIndent(" ");
      final String prettyJson = encoder.convert(options.data);
      log.d(
        // ignore: unnecessary_null_comparison
        "REQUEST ► ︎ ${options.method != null ? options.method.toUpperCase() : 'METHOD'} ${"${options.baseUrl}${options.path}"}\n\n"
        "Headers:\n"
        "$headerMessage\n"
        "❖ QueryParameters : \n"
        "Body: $prettyJson",
      );
    } catch (e) {
      log.e("Failed to extract json request $e");
    }
    super.onRequest(options, handler);
  }

  @override
  void onError(DioException e, ErrorInterceptorHandler handler) async {
    log.e(
      "<-- ${e.message} ${e.response?.requestOptions != null ? (e.response!.requestOptions.baseUrl + e.response!.requestOptions.path) : 'URL'}\n\n"
      "${e.response != null ? e.response!.data : 'Unknown Error'}",
    );

    if (e.response?.statusCode == 401) {
      // If a 401 response is received, refresh the access token
      String newAccessToken = await refreshToken();

      // Update the request header with the new access token
      e.requestOptions.headers['Authorization'] = 'Bearer $newAccessToken';

      // Repeat the request with the updated header
      return handler.resolve(await dio.fetch(e.requestOptions));
    }

    super.onError(e, handler);
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

  Future<String> refreshToken() async {
    // Perform a request to the refresh token endpoint and return the new access token.
    // You can replace this with your own implementation.
    return 'your_new_access_token';
  }
}
