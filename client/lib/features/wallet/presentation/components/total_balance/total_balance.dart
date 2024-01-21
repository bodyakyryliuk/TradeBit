import 'package:cointrade/features/wallet/presentation/components/total_balance/cubit/total_balance_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class TotalBalance extends StatelessWidget {
  const TotalBalance({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<TotalBalanceCubit, TotalBalanceState>(
      builder: (context, state) {
        return state.when<Widget>(
            loading: () => const SizedBox(
                height: 20,
                width: 20,
                child: CircularProgressIndicator()),
            success: (totalBalanceResponseModel) {
              return Text(
                '${totalBalanceResponseModel.balance!.toStringAsFixed(2)} USDT',
                style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 30),
              );
            },
            initial: () {
              return Container();
            },
            failure: (String message) {
              return Text(message);
            });
      },
    );
  }
}
