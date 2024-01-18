import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';

class AuthLocalDataSource {
  void saveAccessToken(String? accessToken) {
    HiveBoxes.appStorageBox.put(DbKeys.accessTokenKey, accessToken);
  }
  void saveRefreshToken(String? refreshToken) {
    HiveBoxes.appStorageBox.put(DbKeys.refreshTokenKey, refreshToken);
  }

  String? getRefreshToken() {
    return HiveBoxes.appStorageBox.get(DbKeys.refreshTokenKey);
  }
}
