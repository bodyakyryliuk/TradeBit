import 'package:bloc/bloc.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'buy_sell_trading_pair_price_converter_state.dart';

part 'buy_sell_trading_pair_price_converter_cubit.freezed.dart';

class BuySellTradingPairPriceConverterCubit
    extends Cubit<BuySellTradingPairPriceConverterState> {
  BuySellTradingPairPriceConverterCubit()
      : super(const BuySellTradingPairPriceConverterState.initial());

  late double price;

  void updatePrice(String price) {
    this.price = double.tryParse(price) ?? 0;
  }

  void convertFromAmount(String amount) {
    final double amountDouble = double.tryParse(amount) ?? 0;
    final double total = amountDouble * price;
    emit(BuySellTradingPairPriceConverterState.updateTotal(total));
  }

  void convertFromTotal(String total) {
    final double totalDouble = double.tryParse(total) ?? 0;
    final double amount = totalDouble / price;
    emit(BuySellTradingPairPriceConverterState.updateAmount(amount));
  }
}
