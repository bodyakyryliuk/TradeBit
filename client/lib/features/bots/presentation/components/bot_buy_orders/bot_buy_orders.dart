import 'package:cointrade/features/bots/presentation/components/bot_buy_order_tile.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BotBuyOrders extends StatelessWidget {
  const BotBuyOrders({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<BotBuyOrdersCubit, BotBuyOrdersState>(
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
          success: (botBuyOrdersResponseModel) {
            if (botBuyOrdersResponseModel.botBuyOrders!.isEmpty) {
              return const Padding(
                padding: EdgeInsets.all(20.0),
                child: Text('No buy orders'),
              );
            }

            return ListView.separated(
              shrinkWrap: true,
              itemCount: botBuyOrdersResponseModel.botBuyOrders!.length,
              itemBuilder: (context, index) {
                final botBuyOrder =
                    botBuyOrdersResponseModel.botBuyOrders![index];
                return BotBuyOrderTile(botBuyOrder: botBuyOrder);
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
