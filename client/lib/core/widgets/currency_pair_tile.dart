import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/cryptocurrency_pair_detailed/cryptocurrency_pair_detailed_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class CurrencyPairTile extends StatelessWidget {
  const CurrencyPairTile(
      {Key? key, required this.titleText, required this.price})
      : super(key: key);

  final String titleText;
  final double price;

  @override
  Widget build(BuildContext context) {
    return ListTile(
        onTap: () {

          context.push(
            '/cryptocurrency-pair-detailed/$titleText',
          );
        },
        contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 2),
        title: Text(
          titleText,
          style: const TextStyle(
              color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16),
        ),
        trailing: Text(
          price.toString(),
          style: const TextStyle(color: Colors.white70, fontSize: 16),
        ));
  }
}
