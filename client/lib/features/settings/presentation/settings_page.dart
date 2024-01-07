import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/features/settings/presentation/components/settings_tile.dart';
import 'package:flutter/material.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Settings'),
        ),
        body: ListView(
          children: ListTile.divideTiles(color: Colors.grey[900],
              context: context,
              tiles: [
                SettingsTile(
                    titleText: 'Log out', isDestructive: true, onPressed: () {
                      HiveBoxes.appStorageBox.delete(DbKeys.accessTokenKey);
                }),
              ]).toList(),
        ));
  }
}
