import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/bot_buy_orders.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BotOrders extends StatefulWidget {
  const BotOrders({Key? key}) : super(key: key);

  @override
  _BotOrdersState createState() => _BotOrdersState();
}

class _BotOrdersState extends State<BotOrders> {

  int selectedSegment = 0;
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        CupertinoSlidingSegmentedControl(
          groupValue: selectedSegment,
          children: const {
            0: Text('Buy orders'),
            1: Text('Sell orders'),
          },
          onValueChanged: (int? value) {
            setState(() {
              selectedSegment = value!;
            });
          },
          thumbColor: Colors.grey[900]!,
          backgroundColor: CupertinoColors.darkBackgroundGray,
        ),
        const SizedBox(height: 10),
        if(selectedSegment == 0)
          const BotBuyOrders()
        else
          const Text('Sell orders')
      ],
    );
  }
}
