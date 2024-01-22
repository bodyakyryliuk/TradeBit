import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class HistoricalPricesPeriodControl extends StatefulWidget {
  const HistoricalPricesPeriodControl({super.key, required this.currencyPair});

  final String currencyPair;

  @override
  State<HistoricalPricesPeriodControl> createState() =>
      _HistoricalPricesPeriodControlState();
}

class _HistoricalPricesPeriodControlState
    extends State<HistoricalPricesPeriodControl> {

  int selectedSegment = 0;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: CupertinoSlidingSegmentedControl(
        // padding: const EdgeInsets.all(10),
        children: const {
          0: Text('1h'),
          1: Text('1d'),
          2: Text('3d'),
          3: Text('1w'),
          4: Text('1m'),
        },
        thumbColor: Colors.grey[900]!,
        backgroundColor: CupertinoColors.darkBackgroundGray,
        onValueChanged: (value) {
          int period = 1;
          switch (value) {
            case 0:
              period = 1;
              break;
            case 1:
              period = 24;
              break;
            case 2:
              period = 72;
              break;
            case 3:
              period = 168;
              break;
            case 4:
              period = 720;
              break;
          }

          context.read<HistoricalPricesCubit>().fetchHistoricalPrices(
              currencyPair: widget.currencyPair, period: period);

          setState(() {
            selectedSegment = value!;
          });
        },
        groupValue: selectedSegment,
      ),
    );
  }
}
