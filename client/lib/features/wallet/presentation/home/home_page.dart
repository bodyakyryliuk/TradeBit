import 'package:cointrade/core/widgets/text_divider.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/all_cryptocurrencies.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/cubit/all_cryptocurrencies_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/total_balance/cubit/total_balance_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/total_balance/total_balance.dart';
import 'package:cointrade/features/wallet/presentation/components/wallet/cubit/wallet_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/wallet/wallet.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  void initState() {
    fetchDashboardData();
    super.initState();
  }

  Future fetchDashboardData() async {
    return Future.wait([
      context.read<TotalBalanceCubit>().fetchTotalBalance(),
      context.read<WalletCubit>().fetchWallet(),
      context.read<AllCryptocurrenciesCubit>().fetchAllCryptocurrencies(),
    ]);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: RefreshIndicator(
          onRefresh: () async {
            return await fetchDashboardData();
          },
          child: ListView(
            children: const [
              Padding(
                padding: EdgeInsets.symmetric(vertical: 80.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    TotalBalance(),
                  ],
                ),
              ),
              TextDivider(titleText: 'Wallet'),
              SizedBox(height: 15),
              Wallet(),
              SizedBox(height: 15),
              TextDivider(titleText: 'All cryptocurrencies'),
              SizedBox(height: 15),
              AllCryptocurrencies(),
            ],
          ),
        ),
      ),
    );
  }
}
