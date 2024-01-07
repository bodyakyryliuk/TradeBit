import 'package:cointrade/app.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/di/injector.dart';
import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';

Future<void> main() async {
  await Hive.initFlutter();
  Hive.openBox(HiveBoxes.appStorageBoxName,
      encryptionCipher: HiveAesCipher(Hive.generateSecureKey()));
  WidgetsFlutterBinding.ensureInitialized();
  await initDependencies();
  runApp(const App());
}
