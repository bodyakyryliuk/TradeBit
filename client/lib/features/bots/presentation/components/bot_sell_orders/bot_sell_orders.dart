import 'package:cointrade/features/bots/presentation/components/bot_order_tile.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_sell_orders/cubit/bot_sell_orders_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BotSellOrders extends StatelessWidget {
  const BotSellOrders({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<BotSellOrdersCubit, BotSellOrdersState>(
      builder: (context, state) {
        return state.when(
          initial: () {
            return const SizedBox();
          },
          loading: () {
            return const Padding(
              padding: EdgeInsets.all(20.0),
              child: SizedBox(
                  height: 20, width: 20, child: CircularProgressIndicator()),
            );
          },
          success: (botSellOrdersResponseModel) {
            if (botSellOrdersResponseModel.botSellOrders!.isEmpty) {
              return const Padding(
                padding: EdgeInsets.all(20.0),
                child: Text('No sell orders'),
              );
            }

            return ListView.separated(
              shrinkWrap: true,
              physics: const ClampingScrollPhysics(),
              itemCount: botSellOrdersResponseModel.botSellOrders!.length,
              itemBuilder: (context, index) {
                final botSellOrder =
                    botSellOrdersResponseModel.botSellOrders![index];
                return BotOrderTile(
                  price: botSellOrder.sellPrice!,
                  quantity: botSellOrder.quantity!,
                  timestamp: botSellOrder.timestamp!,
                  profit: botSellOrder.profit,
                );
                return Text(botSellOrder.profit!.toString());
              },
              separatorBuilder: (BuildContext context, int index) {
                return const SizedBox(height: 10);
              },
            );
          },
          failure: (message) {
            return Text(message);
          },
        );
      },
    );
  }
}
