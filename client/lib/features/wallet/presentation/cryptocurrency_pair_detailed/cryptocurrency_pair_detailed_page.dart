import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/buy_sell.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/current_price_trading_pair_cubit/current_price_trading_pair_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/historical_prices.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/historical_prices_period_control.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

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
        .fetchHistoricalPrices(currencyPair: widget.currencyPair, period: 1);
    context
        .read<CurrentPriceTradingPairCubit>()
        .fetchCurrentPriceForTradingPair(tradingPair: widget.currencyPair);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.currencyPair),
      ),
      body: Column(
        children: [
          const SizedBox(height: 5),
          HistoricalPricesPeriodControl(currencyPair: widget.currencyPair),
          const SizedBox(height: 20),
          const Expanded(child: HistoricalPrices()),
          const SizedBox(height: 20),
          BuySell(currencyPair: widget.currencyPair),
        ],
      ),
    );
  }
}
