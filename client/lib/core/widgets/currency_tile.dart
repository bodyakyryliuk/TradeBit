import 'package:flutter/material.dart';

class CurrencyTile extends StatelessWidget {
  const CurrencyTile(
      {Key? key,
      required this.asset,
      required this.free,
      required this.priceChange})
      : super(key: key);

  final String asset;
  final double free;

  final double priceChange;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 2),
      leading: imageAsset == null
          ? null
          : CircleAvatar(radius: 25,
              backgroundColor: Colors.transparent,
              child: Image.asset(
                imageAsset!,
                fit: BoxFit.cover,
              ),
            ),
      title: Text(
        asset,
        style: const TextStyle(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
      ),
      subtitle: Text(
        free.toString(),
        style: const TextStyle(color: Colors.white70, fontSize: 16),
      ),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(
            priceChangePrefixIcon,
            color: priceChangeColor,
          ),
          const SizedBox(width: 5),
          Text(
            priceChange.toStringAsFixed(4) + '%',
            style: TextStyle(fontSize: 16, color: priceChangeColor),
          ),
        ],
      ),
    );
  }

  IconData? get priceChangePrefixIcon {
    if (priceChange == 0.0) {
      return null;
    } else if (priceChange > 0) {
      return Icons.trending_up;
    } else {
      return Icons.trending_down;
    }
  }

  Color get priceChangeColor {
    if (priceChange == 0.0) {
      return Colors.white70;
    } else if (priceChange > 0) {
      return Colors.green;
    } else {
      return Colors.red;
    }
  }

  String? get imageAsset {
    switch (asset) {
      case 'BTC':
        return 'assets/images/btc.webp';
      case 'USDT':
        return 'assets/images/usdt.webp';
    }
    return null;
  }
}
