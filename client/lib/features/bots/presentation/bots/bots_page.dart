import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class BotsPage extends StatefulWidget {
  const BotsPage({Key? key}) : super(key: key);

  @override
  _BotsPageState createState() => _BotsPageState();
}

class _BotsPageState extends State<BotsPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Bots'),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      floatingActionButton: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20.0),
        child: CommonTextButton(
          title: 'Add bot',
          onPressed: () {
            context.push(Routes.addBot.path);
          },
        ),
      ),
    );
  }
}
