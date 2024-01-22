import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/converter_cubit/buy_sell_trading_pair_price_converter_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/current_price_trading_pair_cubit/current_price_trading_pair_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/current_price_trading_pair_cubit/current_price_trading_pair_cubit.dart';
import 'package:decimal/decimal.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BuySell extends StatefulWidget {
  const BuySell({Key? key, required this.currencyPair}) : super(key: key);

  final String currencyPair;

  @override
  State<BuySell> createState() => _BuySellState();
}

class _BuySellState extends State<BuySell> {
  // BTC, ETH etc
  final TextEditingController _amountController = TextEditingController();

  // Usually in USDT
  final TextEditingController _totalController = TextEditingController();

  final FocusNode _amountFocusNode = FocusNode();
  final FocusNode _totalFocusNode = FocusNode();

  @override
  void initState() {
    _amountController.text = '0';
    _totalController.text = '0';

    _amountFocusNode.addListener(() {
      if (!_amountFocusNode.hasFocus) {
        if (_amountController.text.isEmpty) {
          _amountController.text = '0';
        }
      }
    });

    _totalFocusNode.addListener(() {
      if (!_totalFocusNode.hasFocus) {
        if (_totalController.text.isEmpty) {
          _totalController.text = '0';
        }
      }
    });

    _amountController.addListener(() {
      if (_amountFocusNode.hasFocus) {
        context
            .read<BuySellTradingPairPriceConverterCubit>()
            .convertFromAmount(_amountController.text);
      }
    });
    _totalController.addListener(() {
      if (_totalFocusNode.hasFocus) {
        context
            .read<BuySellTradingPairPriceConverterCubit>()
            .convertFromTotal(_totalController.text);
      }
    });
    super.initState();
  }

  @override
  void dispose() {
    _amountController.dispose();
    _totalController.dispose();
    _amountFocusNode.dispose();
    _totalFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocConsumer<CurrentPriceTradingPairCubit,
        CurrentPriceTradingPairState>(
      builder: (context, state) {
        Widget child = Container(
          color: Colors.black,
          padding: const EdgeInsets.symmetric(horizontal: 20.0),
          child: BlocListener<BuySellTradingPairPriceConverterCubit,
              BuySellTradingPairPriceConverterState>(
            listener: (context, state) {
              state.whenOrNull(
                updateAmount: (amount) {
                  _amountController.text =
                      Decimal.parse(amount.toString()).toString();
                },
                updateTotal: (total) {
                  _totalController.text =
                      Decimal.parse(total.toString()).toString();
                },
              );
            },
            child: Column(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                CommonTextFormField(
                  prefixText: '${widget.currencyPair.split('USDT')[0]}  ',
                  keyboardType: TextInputType.number,
                  controller: _amountController,
                  focusNode: _amountFocusNode,
                ),
                const SizedBox(height: 10),
                CommonTextFormField(
                  prefixText: 'USDT  ',
                  focusNode: _totalFocusNode,
                  controller: _totalController,
                  keyboardType: TextInputType.number,
                ),
                const SizedBox(height: 10),
                const Row(
                  children: [
                    Expanded(
                        child: CommonTextButton(
                      title: 'Buy',
                      color: Colors.green,
                    )),
                    SizedBox(width: 10),
                    Expanded(
                        child: CommonTextButton(
                      title: 'Sell',
                      color: Colors.red,
                    )),
                  ],
                ),
              ],
            ),
          ),
        );

        Widget disabledChild =
            IgnorePointer(child: Opacity(opacity: 0.6, child: child));

        return state.when(
            initial: () => disabledChild,
            loading: () => disabledChild,
            success: (currentPriceTradingPairResponseModel) {
              return child;
            },
            failure: (String message) => disabledChild);
      }, listener: (BuildContext context, CurrentPriceTradingPairState state) {
        state.whenOrNull(
          success: (currentPriceTradingPairResponseModel) {
            context
                .read<BuySellTradingPairPriceConverterCubit>()
                .updatePrice(currentPriceTradingPairResponseModel.price);
          },
          failure: (message) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(message),
                backgroundColor: Colors.red,
              ),
            );
          },
        );
    },
    );
  }
}
