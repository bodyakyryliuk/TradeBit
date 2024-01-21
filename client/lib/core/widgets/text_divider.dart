import 'package:flutter/material.dart';

class TextDivider extends StatelessWidget {
  final String titleText;

  const TextDivider({Key? key, required this.titleText}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 20.0),
      child: Text(
        titleText,
        textAlign: TextAlign.left,
        style: const TextStyle(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 20),
      ),
    );
  }
}
