import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:flutter/material.dart';

class BotOrderTile extends StatelessWidget {
  final double price;
  final DateTime timestamp;
  final double quantity;
  final double? profit;

  const BotOrderTile(
      {super.key,
      required this.price,
      required this.timestamp,
      required this.quantity,
      this.profit});

  @override
  Widget build(BuildContext context) {
    String dateSlug =
        "${timestamp.day.toString()}.${timestamp.month.toString().padLeft(2, '0')}.${timestamp.year.toString().padLeft(2, '0')}";
    String dateHourSlug =
        "${timestamp.hour.toString()}:${timestamp.minute.toString().padLeft(2, '0')}:${timestamp.second.toString().padLeft(2, '0')}";
    return Container(
      padding: const EdgeInsets.all(3),
      decoration: BoxDecoration(
        color: Colors.grey[900]!,
        borderRadius: const BorderRadius.all(Radius.circular(10)),
      ),
      child: ListTile(
        leading: const Icon(
          Icons.currency_exchange,
          color: Colors.white70,
        ),
        title: Text(
          'Price: $price',
          style: const TextStyle(color: Colors.white, fontSize: 16),
        ),
        minVerticalPadding: 10,
        subtitle: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (profit != null) ...[
              const SizedBox(height: 3),
            ],
            Text('Quantity: $quantity',
                style: const TextStyle(color: Colors.white70, fontSize: 14)),
            if (profit != null) ...[
              const SizedBox(height: 3),
              Row(
                children: [
                  const Text('Profit: ',
                      style: TextStyle(color: Colors.white70, fontSize: 14)),
                  Text(profit!.toStringAsFixed(5),
                      style: TextStyle(
                          color: profit == 0.0
                              ? Colors.white70
                              : profit! > 0
                                  ? Colors.green
                                  : Colors.red,
                          fontSize: 14)),
                ],
              )
            ],
          ],
        ),
        trailing: Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(dateSlug,
                style: const TextStyle(color: Colors.white70, fontSize: 14)),
            const SizedBox(height: 5),
            Text(dateHourSlug,
                style: const TextStyle(color: Colors.white70, fontSize: 14)),
          ],
        ),
      ),
    );
  }
}
