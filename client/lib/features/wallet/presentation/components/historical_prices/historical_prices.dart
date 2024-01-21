import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:interactive_chart/interactive_chart.dart';

class HistoricalPrices extends StatelessWidget {
  const HistoricalPrices({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<HistoricalPricesCubit, HistoricalPricesState>(
      builder: (context, state) {
        return state.when<Widget>(
            loading: () => const Padding(
              padding: EdgeInsets.all(30.0),
              child: SizedBox(
                  height: 20, width: 20, child: FittedBox(child: CircularProgressIndicator())),
            ),
            success:
                (HistoricalPricesResponseModel historicalPricesResponseModel) {
              final List<CandleData> data = historicalPricesResponseModel
                  .historicalPrices
                  .map((historicalPrice) {
                return CandleData(
                  timestamp: historicalPrice.timestamp!,
                  open: historicalPrice.openPrice!,
                  high: historicalPrice.highPrice!,
                  low: historicalPrice.lowPrice!,
                  close: historicalPrice.closePrice!,
                  volume: historicalPrice.volume!,
                );
              }).toList();

              return SizedBox(
                height: 400,
                child: InteractiveChart(
                  style: ChartStyle(volumeHeightFactor: 0.1,
                      priceLossColor: Colors.red,
                      priceGainColor: Colors.green,
                      priceGridLineColor: Colors.grey[800]!,
                      timeLabelHeight: 0.0,
                      volumeColor: Colors.transparent,),initialVisibleCandleCount: 35,
                  candles: data,
                ),
              );
            },
            initial: () {
              return const SizedBox();
            },
            failure: (String message) {
              return Text(message);
            });
      },
    );
  }
}
