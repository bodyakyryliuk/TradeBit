import 'package:flutter/material.dart';

class BotDetailCell extends StatelessWidget {
  const BotDetailCell({super.key, required this.titleText, required this.valueText});

  final String titleText;
  final String valueText;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: Colors.grey[900],
        border: Border.all(color: Colors.grey[800]!),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.min,
        children:[
          Text(valueText, style: const TextStyle(fontSize: 16,color: Colors.white,fontWeight: FontWeight.bold)),
          const SizedBox(height: 5),
          Text(titleText, style: const TextStyle(fontSize: 15,color: Colors.grey,fontWeight: FontWeight.bold)),
        ],
      )
    );
  }
}
