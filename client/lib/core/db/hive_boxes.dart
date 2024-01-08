import 'package:hive/hive.dart';

class HiveBoxes {
  static const String appStorageBoxName = 'app_storage_box';
  static Box appStorageBox = Hive.box(appStorageBoxName);
}
