import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/features/bots/data/models/predictions_response_model.dart';
import 'package:cointrade/features/bots/presentation/components/predictions/cubit/predictions_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:syncfusion_flutter_charts/charts.dart';

class Predictions extends StatelessWidget {
  const Predictions({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<PredictionsCubit, PredictionsState>(
      builder: (context, state) {
        return SizedBox(
          height: 300,
          child: Center(
            child: state.when(
              initial: () => const Center(child: CircularProgressIndicator()),
              loading: () => const Center(child: CircularProgressIndicator()),
              success: (predictionsResponseModel) {
                return SfCartesianChart(

                  primaryXAxis: CategoryAxis(
                    majorTickLines: MajorTickLines(color: Colors.grey[900]!,width: 1),
                    majorGridLines:  MajorGridLines(color: Colors.grey[900]!,width: 1),
                    minorGridLines:  MinorGridLines(color: Colors.grey[900]!,width: 1),
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
                    )
                  ],
                );
              },
              failure: (message) => Text(message),
            ),
          ),
        );
      },
    );
  }
}
