import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:syncfusion_flutter_charts/charts.dart';
import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/features/bots/data/models/predictions_response_model.dart';
import 'package:cointrade/features/bots/presentation/components/predictions/cubit/predictions_cubit.dart';
import 'package:cointrade/features/wallet/data/models/historical_prices_response_model.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';

class Predictions extends StatelessWidget {
  const Predictions({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<PredictionsCubit, PredictionsState>(
      builder: (context, state) {
        return SizedBox(
          height: 400,
          child: Center(
            child: state.maybeWhen(
              initial: () => const CircularProgressIndicator(),
              loading: () => const CircularProgressIndicator(),
              success: (predictionsResponseModel) {
                return BlocBuilder<HistoricalPricesCubit,
                    HistoricalPricesState>(
                  builder: (context, historicalPricesState) {
                    bool showHistoricalPrices = historicalPricesState.maybeMap(
                      success: (historicalPricesResponseModel) => true,
                      orElse: () => false,
                    );

                    return SfCartesianChart(
                      zoomPanBehavior: ZoomPanBehavior(
                          enablePanning: true,
                          enablePinching: true,
                          enableDoubleTapZooming: true,
                          zoomMode: ZoomMode.x),
                      primaryXAxis: CategoryAxis(
                        interval: 30,
                        isInversed: true,
                        majorTickLines: MajorTickLines(
                          color: Colors.grey[900]!,
                          width: 1,
                        ),
                        majorGridLines: MajorGridLines(
                          color: Colors.grey[900]!,
                          width: 1,
                        ),
                        minorGridLines: MinorGridLines(
                          color: Colors.grey[900]!,
                          width: 1,
                        ),
                      ),
                      series: <ChartSeries>[
                        LineSeries<Prediction, DateTime>(
                          enableTooltip: true,
                          color: context.theme.primaryColor,
                          dataSource: predictionsResponseModel.predictions,
                          xValueMapper: (Prediction prediction, _) =>
                              prediction.timestamp!,
                          yValueMapper: (Prediction prediction, _) =>
                              prediction.predictedPrice,
                        ),
                        if (showHistoricalPrices)
                          LineSeries<HistoricalPrice, DateTime>(
                            enableTooltip: true,
                            color: Colors.grey,
                            dataSource: historicalPricesState.maybeMap(
                              success: (state) => state
                                  .historicalPricesResponseModel
                                  .historicalPrices,
                              orElse: () => [],
                            ),
                            xValueMapper:
                                (HistoricalPrice historicalPrice, _) =>
                                    DateTime.fromMillisecondsSinceEpoch(
                                        historicalPrice.timestamp!),
                            yValueMapper:
                                (HistoricalPrice historicalPrice, _) =>
                                    historicalPrice.highPrice,
                          ),
                      ],
                    );
                  },
                );
              },
              failure: (message) => const Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.area_chart_sharp, size: 50, color: Colors.white70),
                  const SizedBox(height: 10),
                  Text(
                    'Prices prediction not found',
                    style: const TextStyle(color: Colors.white70, fontSize: 18),
                  ),
                ],
              ),
              orElse: () => const CircularProgressIndicator(),
            ),
          ),
        );
      },
    );
  }
}
