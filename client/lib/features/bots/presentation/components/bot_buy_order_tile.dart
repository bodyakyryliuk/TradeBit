import 'package:cointrade/features/bots/data/models/bot_buy_orders_response_model.dart';
import 'package:flutter/material.dart';

class BotBuyOrderTile extends StatelessWidget {
  const BotBuyOrderTile({Key? key, required this.botBuyOrder})
      : super(key: key);

  final BotBuyOrder botBuyOrder;

  @override
  Widget build(BuildContext context) {
    String dateSlug ="${botBuyOrder.timestamp!.day.toString()}.${botBuyOrder.timestamp!.month.toString().padLeft(2,'0')}.${botBuyOrder.timestamp!.year.toString().padLeft(2,'0')}";
    String dateHourSlug ="${botBuyOrder.timestamp!.hour.toString()}:${botBuyOrder.timestamp!.minute.toString().padLeft(2,'0')}:${botBuyOrder.timestamp!.second.toString().padLeft(2,'0')}";
    return Container(
      padding: const EdgeInsets.all(3),
      decoration:  BoxDecoration(
        color: Colors.grey[900]!,
        borderRadius: const BorderRadius.all(Radius.circular(10)),
      ),
      child: ListTile(
        leading: const Icon(Icons.currency_exchange, color: Colors.white70  ,),
        title: Text(
          'Price: ${botBuyOrder.buyPrice!}',
          style: const TextStyle(color: Colors.white, fontSize: 16),
        ),
        subtitle: Text('Quantity: ${botBuyOrder.quantity}',
            style: const TextStyle(color: Colors.white70, fontSize: 14)),

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
