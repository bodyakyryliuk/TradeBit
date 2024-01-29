import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/bot_buy_orders.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_detail_cell.dart';
import 'package:cointrade/features/bots/presentation/components/bot_orders.dart';
import 'package:cointrade/features/bots/presentation/components/bot_sell_orders/cubit/bot_sell_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/predictions/cubit/predictions_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/predictions/predictions.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BotDetailedPage extends StatefulWidget {
  const BotDetailedPage(
      {Key? key, required this.botId, required this.tradingPair})
      : super(key: key);

  final int botId;
  final String tradingPair;

  @override
  _BotDetailedPageState createState() => _BotDetailedPageState();
}

class _BotDetailedPageState extends State<BotDetailedPage> {
  @override
  void initState() {
    _refresh();
    super.initState();
  }

  Future _refresh() {
    return Future.wait(
      [
        context.read<BotBuyOrdersCubit>().fetchBotBuyOrders(widget.botId),
        context.read<BotSellOrdersCubit>().fetchBotSellOrders(widget.botId),
        context.read<PredictionsCubit>().fetchPredictions(widget.tradingPair),
        context.read<HistoricalPricesCubit>().fetchHistoricalPrices(
            currencyPair: widget.tradingPair, period: 24 * 7),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<BotsCubit, BotsState>(
      builder: (context, state) {
        final BotModel bot =
            state.bots.firstWhere((bot) => bot.id == widget.botId);
        return Scaffold(
          appBar: AppBar(
            title: Text(bot.name!),
            actions: [
              Switch.adaptive(
                  value: bot.enabled!,
                  onChanged: (bool value) {
                    context.read<BotsCubit>().toggleBotEnabled(bot.id!);
                  })
            ],
          ),
          body: RefreshIndicator(
            onRefresh: () {
              return _refresh();
            },
            child: ListView(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              children: [
                const Predictions(),
                const SizedBox(height: 20),
                GridView.count(
                  physics: const ClampingScrollPhysics(),
                  crossAxisCount: 3,
                  shrinkWrap: true,
                  childAspectRatio: 1.5,
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                  children: [
                    BotDetailCell(
                      titleText: 'Buy threshold',
                      valueText: bot.buyThreshold!.toString(),
                    ),
                    BotDetailCell(
                      titleText: 'Sell threshold',
                      valueText: bot.sellThreshold!.toString(),
                    ),
                    BotDetailCell(
                      titleText: 'Take profit',
                      valueText: '${bot.takeProfitPercentage!}%',
                    ),
                    BotDetailCell(
                      titleText: 'Stop loss',
                      valueText: '${bot.stopLossPercentage!}%',
                    ),
                    BotDetailCell(
                      titleText: 'Trade size',
                      valueText: bot.tradeSize!.toString(),
                    ),
                    BotDetailCell(
                      titleText: 'Trading pair',
                      valueText: bot.tradingPair!,
                    ),
                  ],
                ),
                const SizedBox(height: 20),
                const BotOrders(),
              ],
            ),
          ),
        );
      },
    );
  }
}
