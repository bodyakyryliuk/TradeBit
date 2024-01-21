import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/historical_prices.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class CryptocurrencyPairDetailedArgs {
  final String currencyPair;
  final double price;

  CryptocurrencyPairDetailedArgs(
      {required this.currencyPair, required this.price});
}

class CryptocurrencyPairDetailedPage extends StatefulWidget {
  const CryptocurrencyPairDetailedPage({Key? key, required this.currencyPair})
      : super(key: key);

  final String currencyPair;

  @override
  _CryptocurrencyPairDetailedPageState createState() =>
      _CryptocurrencyPairDetailedPageState();
}

class _CryptocurrencyPairDetailedPageState
    extends State<CryptocurrencyPairDetailedPage> {
  @override
  void initState() {
    context
        .read<HistoricalPricesCubit>()
        .fetchHistoricalPrices(currencyPair: widget.currencyPair, period: 168);
    super.initState();
  }

  int selectedSegment = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.currencyPair),
      ),
      body: ListView(
        children: [
          Padding(
            padding: const EdgeInsets.all(20.0),
            child: CupertinoSlidingSegmentedControl(
              padding: const EdgeInsets.all(10),
              children: const {
                0: Text('1h'),
                1: Text('1d'),
                2: Text('3d'),
                3: Text('1w'),
                4: Text('1m'),
              },
              thumbColor: Colors.grey[900]!,
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
          ),
          const HistoricalPrices()
        ],
      ),
    );
  }
}
