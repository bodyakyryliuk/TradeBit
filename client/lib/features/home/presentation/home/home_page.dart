import 'package:cointrade/features/home/presentation/total_balance/cubit/total_balance_cubit.dart';
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
    context.read<TotalBalanceCubit>().fetchTotalBalance();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 80.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                BlocBuilder<TotalBalanceCubit, TotalBalanceState>(
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
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
