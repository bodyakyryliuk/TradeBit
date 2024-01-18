import 'package:cointrade/core/api/end_points.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/error/failures.dart';
import 'package:cointrade/core/api/dio_interceptor.dart';
import 'package:dio/dio.dart';

class DioClient {
  String? _authToken;
  bool _isUnitTest = false;
  late Dio _dio;

  DioClient({bool isUnitTest = false}) {
    _isUnitTest = isUnitTest;
    try {
      _authToken = HiveBoxes.appStorageBox.get(DbKeys.accessTokenKey);
    } catch (_) {}
    _dio = _createDio();
    if (!_isUnitTest) _dio.interceptors.add(DioInterceptor(accessToken: _authToken));
  }

  Dio get dio {
    if (_isUnitTest) {
      return _dio;
    } else {
      try {
        _authToken = HiveBoxes.appStorageBox.get(DbKeys.accessTokenKey);
      } catch (_) {}
      final _dio = _createDio();
      if (!_isUnitTest) _dio.interceptors.add(DioInterceptor(accessToken: _authToken));
      return _dio;
    }
  }

  Dio _createDio() => Dio(BaseOptions(
      baseUrl: EndPoints.baseUrl,
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        // if (_authToken != null) ...{
        //   "Authorization": _authToken,
        // }
      },
      receiveTimeout: const Duration(seconds: 25),
      connectTimeout: const Duration(seconds: 25),
      validateStatus: (int? status) {
        return status! > 0;
      }));

  Future<Response> getRequest(
    String url, {
    Map<String, dynamic>? queryParameters,
  }) async {
    try {
      return await dio.get(url, queryParameters: queryParameters);
    } on DioException catch (e) {
      throw Exception(e.message);
    }
  }

  Future<Response> postRequest(
    String url, {
    Map<String, dynamic>? data,
  }) async {
    try {
      return await dio.post(url, data: data);
    } on DioException catch (e) {
      throw ServerFailure(e.message);
    }
  }
}
