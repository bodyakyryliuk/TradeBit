import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class LandingPage extends StatelessWidget {
  const LandingPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
            const Spacer(),
            const Text(
              'Welcome to',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.w500),
            ),
            const SizedBox(height: 6),
            Text(
              'TradeBit',
              style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: Theme.of(context).primaryColor),
            ),
            const SizedBox(height: 40),
            Image.asset('assets/images/crypto-currencies.png', height: 300),
            const Spacer(),
            CommonTextButton(
              title: 'Sign in',
              isPrimary: false,
              onPressed: () {
                context.push(Routes.login.path);
              },
            ),
            const SizedBox(height: 10),
            CommonTextButton(
              title: 'Sign up',
              onPressed: () {
                context.push(Routes.register.path);
              },
            ),
          ]),
        ),
      ),
    );
  }
}
