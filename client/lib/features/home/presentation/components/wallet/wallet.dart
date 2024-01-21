import 'package:cointrade/core/widgets/currency_tile.dart';
import 'package:cointrade/features/home/presentation/components/wallet/cubit/wallet_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class Wallet extends StatelessWidget {
  const Wallet({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<WalletCubit, WalletState>(
      builder: (context, state) {
        return state.when(initial: () {
          return const SizedBox();
        }, loading: () {
          return const SizedBox(
              height: 20,
              width: 20,
              child: FittedBox(child: CircularProgressIndicator()));
        }, success: (walletResponseModel) {
          return ListView.builder(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            itemCount: walletResponseModel.balances!.length,
            itemBuilder: (context, index) {
              var balance = walletResponseModel.balances![index];
              return CurrencyTile(
                  asset: balance.asset!,
                  free: balance.free!,
                  priceChange: balance.priceChange!);

            },
          );
        }, failure: (message) {
          return Text(message);
        });
      },
    );
  }
}
