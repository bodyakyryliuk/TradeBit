import 'package:cointrade/core/di/injector.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:cointrade/features/auth/presentation/reset_password/cubit/reset_password_cubit.dart';
import 'package:cointrade/features/bots/presentation/add_bot/cubit/add_bot_cubit.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_buy_orders/cubit/bot_buy_orders_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_sell_orders/cubit/bot_sell_orders_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/cubit/all_cryptocurrencies_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/converter_cubit/buy_sell_trading_pair_price_converter_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/current_price_trading_pair_cubit/current_price_trading_pair_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/buy_sell/make_order_cubit/make_order_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/total_balance/cubit/total_balance_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/wallet/cubit/wallet_cubit.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/cubit/connect_binance_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BlocsProviders extends StatelessWidget {
  const BlocsProviders({super.key, required this.child});
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: <BlocProvider>[
        BlocProvider<RegisterCubit>(create: (_) => sl<RegisterCubit>()),
        BlocProvider<LoginCubit>(create: (_) => sl<LoginCubit>()),
        BlocProvider<ResetPasswordCubit>(create: (_) => sl<ResetPasswordCubit>()),
        BlocProvider<ConnectBinanceCubit>(create: (_) => sl<ConnectBinanceCubit>()),
        BlocProvider<TotalBalanceCubit>(create: (_) => sl<TotalBalanceCubit>()),
        BlocProvider<WalletCubit>(create: (_) => sl<WalletCubit>()),
        BlocProvider<AllCryptocurrenciesCubit>(create: (_) => sl<AllCryptocurrenciesCubit>()),
        BlocProvider<HistoricalPricesCubit>(create: (_) => sl<HistoricalPricesCubit>()),
        BlocProvider<BuySellTradingPairPriceConverterCubit>(create: (_) => sl<BuySellTradingPairPriceConverterCubit>()),
        BlocProvider<CurrentPriceTradingPairCubit>(create: (_) => sl<CurrentPriceTradingPairCubit>()),
        BlocProvider<MakeOrderCubit>(create: (_) => sl<MakeOrderCubit>()),
        BlocProvider<AddBotCubit>(create: (_) => sl<AddBotCubit>()),
        BlocProvider<BotsCubit>(create: (_) => sl<BotsCubit>()),
        BlocProvider<BotBuyOrdersCubit>(create: (_) => sl<BotBuyOrdersCubit>()),
        BlocProvider<BotSellOrdersCubit>(create: (_) => sl<BotSellOrdersCubit>()),
      ],
      child: child,
    );
  }
}
