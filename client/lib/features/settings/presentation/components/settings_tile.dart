import 'package:flutter/material.dart';

class SettingsTile extends StatelessWidget {
  const SettingsTile(
      {Key? key,
      required this.titleText,
      required this.onPressed,
      this.isDestructive = false})
      : super(key: key);

  final String titleText;
  final bool isDestructive;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: onPressed,
      contentPadding: const EdgeInsets.symmetric(horizontal: 15, vertical: 10),
      title: Text(titleText,
          style: TextStyle(
              fontSize: 20, color: isDestructive ? Colors.red : Colors.white)),
    );
  }
}
