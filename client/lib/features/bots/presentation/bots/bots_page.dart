import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:cointrade/features/bots/presentation/components/bot_tile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class BotsPage extends StatefulWidget {
  const BotsPage({Key? key}) : super(key: key);

  @override
  _BotsPageState createState() => _BotsPageState();
}

class _BotsPageState extends State<BotsPage> {
  @override
  void initState() {
    _refresh();
    super.initState();
  }

  Future<void> _refresh() {
    return context.read<BotsCubit>().fetchBots();
  }

  @override
  Widget build(BuildContext context) {
    return BlocConsumer<BotsCubit, BotsState>(
      builder: (context, state) {
        return RefreshIndicator(
          onRefresh: () {
            return _refresh();
          },
          child: Scaffold(
              appBar: AppBar(
                title: const Text('Bots'),
                actions: [
                  if (state.requested)
                    const Padding(
                      padding: EdgeInsets.only(right: 8.0),
                      child: SizedBox(
                          height: 20,
                          width: 20,
                          child: CircularProgressIndicator()),
                    ),
                ],
              ),
              floatingActionButtonLocation:
                  FloatingActionButtonLocation.centerFloat,
              floatingActionButton: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20.0),
                child: CommonTextButton(
                  title: 'Add bot',
                  onPressed: () {
                    context.push(Routes.addBot.path);
                  },
                ),
              ),
              body: state.bots.isEmpty
                  ? const Center(
                      child: Text(
                      'No bots found',
                      style: TextStyle(fontSize: 20, color: Colors.white70),
                    ))
                  : ListView.builder(
                      itemCount: state.bots.length,
                      itemBuilder: (context, index) {
                        final bot = state.bots[index];
                        return BotTile(bot: bot);
                      },
                    )),
        );
      },
      listener: (BuildContext context, BotsState state) {
        if (state.errorMessage != null) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.errorMessage!),
              backgroundColor: Colors.red,
            ),
          );
        }
      },
    );
  }
}
