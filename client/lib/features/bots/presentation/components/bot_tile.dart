import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class BotTile extends StatelessWidget {
  const BotTile({Key? key, required this.bot}) : super(key: key);

  final BotModel bot;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: (){
        context.push('/bot-detailed/${bot.id}');
      },
      leading:
          const Icon(Icons.smart_toy_outlined, color: Colors.white, size: 30),
      title: bot.name == null
          ? null
          : Text(
              bot.name!,
              style: const TextStyle(color: Colors.white, fontSize: 16),
            ),
      subtitle: bot.tradingPair == null
          ? null
          : Text(bot.tradingPair!,
              style: const TextStyle(color: Colors.white70)),
      trailing: bot.enabled == null
          ? null
          : Switch.adaptive(
              value: bot.enabled!,
              onChanged: (bool? value) {
                context.read<BotsCubit>().toggleBotEnabled(bot.id!);
              },

            ),
    );
  }
}
