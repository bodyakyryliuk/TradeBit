import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/themes/themes.dart';
import 'package:cointrade/core/wrappers/blocs_providers.dart';
import 'package:flutter/material.dart';

class App extends StatelessWidget {
  const App({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'TradeBit',
      debugShowCheckedModeBanner: false,
      darkTheme: darkTheme(context),
      routerConfig: AppRouter.router,
      builder: (context, child) {
        return BlocsProviders(child: child!);
      }
    );
  }
}
