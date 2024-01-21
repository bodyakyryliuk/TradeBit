import 'package:flutter/material.dart';

class CurrencyPairTile extends StatelessWidget {
  const CurrencyPairTile({Key? key, required this.titleText, required this.price}) : super(key: key);

  final String titleText;
  final double price;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 2),
      title: Text(
        titleText,
        style: const TextStyle(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),

      ),

      trailing: Text(
        price.toString(),
        style: const TextStyle(color: Colors.white70, fontSize: 16),
      )
    );
  }
}
