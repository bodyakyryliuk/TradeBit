import 'dart:async';
import 'dart:developer';

import 'package:async/async.dart';
import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/di/injector.dart';
import 'package:cointrade/core/routes/pages/not_found_page.dart';
import 'package:cointrade/features/auth/presentation/email_verification/email_verification_page.dart';
import 'package:cointrade/features/auth/presentation/landing/landing_page.dart';
import 'package:cointrade/features/auth/presentation/login/login_page.dart';
import 'package:cointrade/features/auth/presentation/register/register_page.dart';
import 'package:cointrade/features/auth/presentation/reset_password/reset_password_page.dart';
import 'package:cointrade/features/bots/data/models/bots_response_model.dart';
import 'package:cointrade/features/bots/presentation/add_bot/add_bot_page.dart';
import 'package:cointrade/features/bots/presentation/bot_detailed/bot_detailed_page.dart';
import 'package:cointrade/features/bots/presentation/bots/bots_page.dart';
import 'package:cointrade/features/settings/presentation/top_up/top_up_page.dart';
import 'package:cointrade/features/wallet/presentation/components/historical_prices/cubit/historical_prices_cubit.dart';
import 'package:cointrade/features/wallet/presentation/cryptocurrency_pair_detailed/cryptocurrency_pair_detailed_page.dart';
import 'package:cointrade/features/wallet/presentation/home/home_page.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/connect_binance_page.dart';
import 'package:cointrade/features/settings/presentation/settings_page.dart';
import 'package:cointrade/features/skeleton/presentation/skeleton_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

enum Routes {
  root("/"),
  settings("/settings"),
  connectBinance("/connect-binance"),
  topUp("/top-up"),
  landing("/landing"),
  login("/auth/login"),
  register("/auth/register"),
  resetPassword("/auth/reset-password"),
  emailConfirmation("/auth/email-confirmation"),
  cryptocurrencyPairDetailed("/cryptocurrency-pair-detailed/:currencyPair"),
  bots("/bots"),
  botDetailed("/bot-detailed/:botId/:tradingPair"),
  addBot("/add-bot");

  const Routes(this.path);

  final String path;
}

class AppRouter {
  static final _rootNavigatorKey = GlobalKey<NavigatorState>();

  // static final _shellNavigatorKey = GlobalKey<NavigatorState>();

  static final GoRouter _router = GoRouter(
    debugLogDiagnostics: true,
    navigatorKey: _rootNavigatorKey,
    refreshListenable: GoRouterRefreshStream(StreamGroup.merge([
      HiveBoxes.appStorageBox.watch(key: DbKeys.accessTokenKey),
      HiveBoxes.appStorageBox.watch(key: DbKeys.binanceApiKey),
      HiveBoxes.appStorageBox.watch(key: DbKeys.binanceSecretApiKey),
    ])),
    redirect: (BuildContext context, GoRouterState state) {
      String? accessToken = HiveBoxes.appStorageBox
          .get(DbKeys.accessTokenKey, defaultValue: null);
      String? binanceApiKey =
      HiveBoxes.appStorageBox.get(DbKeys.binanceApiKey, defaultValue: null);
      String? binanceSecretApiKey = HiveBoxes.appStorageBox
          .get(DbKeys.binanceSecretApiKey, defaultValue: null);

      // if the user is not logged in, they need to login
      bool loggedIn = accessToken != null;

      List<String> authRoutesPaths = [
        Routes.landing,
        Routes.register,
        Routes.login,
        Routes.emailConfirmation,
        Routes.resetPassword,
      ].map((e) => e.path).toList();

      final loggingIn = authRoutesPaths.contains(state.fullPath);

      if (!loggedIn) return loggingIn ? null : Routes.landing.path;

      // if logged in but user email is null
      // if (loggedIn && currentUser.email == null) return Routes.provideEmail;

      // if the user is logged in but still on the login page, send them to
      // the home page
      if (loggingIn) {
        if (binanceApiKey == null || binanceSecretApiKey == null) {
          return Routes.connectBinance.path;
        }
        return Routes.root.path;
      }

      // no need to redirect at all
      return null;
    },
    routes: [
      StatefulShellRoute.indexedStack(
          builder: (context, state, child) {
            return SkeletonPage(child: child);
          },
          branches: [
            StatefulShellBranch(routes: [
              GoRoute(
                path: Routes.root.path,
                name: Routes.root.name,
                builder: (context, state) => const HomePage(),
              ),
            ]),
            StatefulShellBranch(routes: [
              GoRoute(
                path: Routes.bots.path,
                name: Routes.bots.name,
                builder: (context, state) => const BotsPage(),
              ),
            ]),
            StatefulShellBranch(routes: [
              GoRoute(
                path: Routes.settings.path,
                name: Routes.settings.name,
                builder: (context, state) => const SettingsPage(),
              ),
            ]),
          ]),
      GoRoute(
        parentNavigatorKey: _rootNavigatorKey,
        path: Routes.connectBinance.path,
        name: Routes.connectBinance.name,
        builder: (context, state) => const ConnectBinancePage(),
      ),
      GoRoute(
        parentNavigatorKey: _rootNavigatorKey,
        path: Routes.topUp.path,
        name: Routes.topUp.name,
        builder: (context, state) => const TopUpPage(),
      ),
      GoRoute(
        parentNavigatorKey: _rootNavigatorKey,
        path: Routes.addBot.path,
        name: Routes.addBot.name,
        builder: (context, state) => const AddBotPage(),
      ),
      GoRoute(
        parentNavigatorKey: _rootNavigatorKey,
        path: Routes.botDetailed.path,
        name: Routes.botDetailed.name,
        builder: (context, state) =>
            BlocProvider(
              create: (context) => sl<HistoricalPricesCubit>(),
              child: BotDetailedPage(
                botId: int.parse(state.pathParameters['botId']!),
                tradingPair: state.pathParameters['tradingPair']!,
              ),
            ),
      ),
      GoRoute(
        parentNavigatorKey: _rootNavigatorKey,
        path: Routes.cryptocurrencyPairDetailed.path,
        name: Routes.cryptocurrencyPairDetailed.name,
        builder: (context, state) {
          return CryptocurrencyPairDetailedPage(
            key: state.pageKey,
            currencyPair: state.pathParameters['currencyPair']!,
          );
        },
      ),
      GoRoute(
        path: Routes.landing.path,
        name: Routes.landing.name,
        builder: (context, state) => const LandingPage(),
      ),
      GoRoute(
        path: Routes.login.path,
        name: Routes.login.name,
        builder: (context, state) => const SignInPage(),
      ),
      GoRoute(
        path: Routes.register.path,
        name: Routes.register.name,
        builder: (context, state) => const SignUpPage(),
      ),
      GoRoute(
        path: Routes.resetPassword.path,
        name: Routes.resetPassword.name,
        builder: (context, state) => const ResetPasswordPage(),
      ),
      GoRoute(
        path: Routes.emailConfirmation.path,
        name: Routes.emailConfirmation.name,
        builder: (context, state) =>
            EmailConfirmationPage(
              email: state.extra as String,
            ),
      ),
    ],
    errorBuilder: (context, state) => const NotFoundPage(),
  );

  static GoRouter get router => _router;
}

class GoRouterRefreshStream extends ChangeNotifier {
  GoRouterRefreshStream(Stream<dynamic> stream) {
    notifyListeners();
    _subscription = stream.asBroadcastStream().listen(
          (dynamic _) => notifyListeners(),
    );
  }

  late final StreamSubscription<dynamic> _subscription;

  @override
  void dispose() {
    _subscription.cancel();
    super.dispose();
  }
}
