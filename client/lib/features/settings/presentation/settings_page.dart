import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/features/settings/presentation/components/settings_tile.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Settings'),
        ),
        body: ListView(
          children: ListTile.divideTiles(
              color: Colors.grey[900],
              context: context,
              tiles: [
                SettingsTile(
                  titleText: 'Connect Binance',
                  onPressed: () {
                    context.push(Routes.connectBinance.path);
                  },
                ),
                SettingsTile(
                  titleText: 'Top up',
                  onPressed: () {
                    context.push(Routes.topUp.path);
                  },
                ),
                SettingsTile(
                    titleText: 'Log out',
                    isDestructive: true,
                    onPressed: () {
                      HiveBoxes.appStorageBox.delete(DbKeys.accessTokenKey);
                      HiveBoxes.appStorageBox.delete(DbKeys.refreshTokenKey);
                      HiveBoxes.appStorageBox
                          .delete(DbKeys.binanceSecretApiKey);
                      HiveBoxes.appStorageBox.delete(DbKeys.binanceApiKey);
                    }),
              ]).toList(),
        ));
  }
}
