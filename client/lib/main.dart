import 'dart:convert';

import 'package:cointrade/app.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/di/injector.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:hive_flutter/hive_flutter.dart';

Future<void> main() async {
  await Hive.initFlutter();
  await createEncryptedBox();
  WidgetsFlutterBinding.ensureInitialized();
  await initDependencies();
  runApp(const App());
}

Future<void> createEncryptedBox() async {
  const secureStorage = FlutterSecureStorage();
  // if key not exists return null
  final encryptionKeyString = await secureStorage.read(key: 'encrypted_box_key');
  if (encryptionKeyString == null) {
    final key = Hive.generateSecureKey();
    await secureStorage.write(
      key: 'encrypted_box_key',
      value: base64UrlEncode(key),
    );
  }
  final key = await secureStorage.read(key: 'encrypted_box_key');
  final encryptionKeyUint8List = base64Url.decode(key!);
  final encryptedBox = await Hive.openBox(HiveBoxes.appStorageBoxName,
      encryptionCipher: HiveAesCipher(encryptionKeyUint8List));
}
