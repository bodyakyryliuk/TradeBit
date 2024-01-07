import 'dart:async';

import 'package:cointrade/core/db/hive_boxes.dart';
import 'package:cointrade/core/db/keys.dart';
import 'package:cointrade/core/routes/pages/not_found_page.dart';
import 'package:cointrade/features/auth/presentation/email_verification/email_verification_page.dart';
import 'package:cointrade/features/auth/presentation/login/login_page.dart';
import 'package:cointrade/features/auth/presentation/register/register_page.dart';
import 'package:cointrade/features/home/home_page.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

enum Routes {
  root("/"),
  login("/auth/login"),
  register("/auth/register"),
  emailConfirmation("/auth/email-confirmation/:email");

  const Routes(this.path);

  final String path;
}

class AppRouter {
  static final _rootNavigatorKey = GlobalKey<NavigatorState>();

  static final GoRouter _router = GoRouter(
    debugLogDiagnostics: true,
    navigatorKey: _rootNavigatorKey,
    initialLocation: Routes.register.path,
    refreshListenable: GoRouterRefreshStream(HiveBoxes.appStorageBox.watch(
      key: DbKeys.accessTokenKey,
    )),
    redirect: (BuildContext context, GoRouterState state) {
      String? accessToken = HiveBoxes.appStorageBox
          .get(DbKeys.accessTokenKey, defaultValue: null);
      // if the user is not logged in, they need to login
      bool loggedIn = accessToken != null;

      List<String> authRoutesPaths = [
        Routes.register,
        Routes.login,
        Routes.emailConfirmation,
      ].map((e) => e.path).toList();

      final loggingIn = authRoutesPaths.contains(state.fullPath);

      if (!loggedIn) return loggingIn ? null : Routes.login.path;

      // if logged in but user email is null
      // if (loggedIn && currentUser.email == null) return Routes.provideEmail;

      // if the user is logged in but still on the login page, send them to
      // the home page
      if (loggingIn) return Routes.root.path;

      // no need to redirect at all
      return null;
    },
    routes: [
      GoRoute(
        path: Routes.root.path,
        name: Routes.root.name,
        builder: (context, state) => const HomePage(),
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
        path: Routes.emailConfirmation.path,
        name: Routes.emailConfirmation.name,
        builder: (context, state) => EmailConfirmationPage(
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
