import 'package:cointrade/core/widgets/currency_pair_tile.dart';
import 'package:cointrade/features/wallet/data/models/all_cryptocurrencies_response_model.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/cubit/all_cryptocurrencies_cubit.dart';
import 'package:cointrade/features/wallet/presentation/components/all_cryptocurrencies/cubit/all_cryptocurrencies_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../../../../core/widgets/currency_tile.dart';

class AllCryptocurrencies extends StatelessWidget {
  const AllCryptocurrencies({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<AllCryptocurrenciesCubit, AllCryptocurrenciesState>(
        builder: (context, state) {
      return state.when(initial: () {
        return const SizedBox();
      }, loading: () {
        return const SizedBox(
            height: 20,
            width: 20,
            child: FittedBox(child: CircularProgressIndicator()));
      }, success:
          (AllCryptocurrenciesResponseModel allCryptocurrenciesResponseModel) {
        return ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: allCryptocurrenciesResponseModel.cryptocurrencies.length,
          itemBuilder: (context, index) {
            var cryptocurrency =
                allCryptocurrenciesResponseModel.cryptocurrencies[index];
            return CurrencyPairTile(
              titleText: cryptocurrency.symbol!,
              price: double.parse(cryptocurrency.price!),
            );
          },
        );
      }, failure: (message) {
        return Text(message);
      });
    });
  }
}
