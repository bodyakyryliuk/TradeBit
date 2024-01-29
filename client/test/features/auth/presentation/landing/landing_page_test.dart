import 'package:cointrade/core/di/injector.dart';
import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/wrappers/blocs_providers.dart';
import 'package:cointrade/features/auth/presentation/landing/landing_page.dart';
import 'package:cointrade/features/auth/presentation/login/login_page.dart';
import 'package:cointrade/features/auth/presentation/register/register_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:go_router/go_router.dart';

main() {
  initDependencies();
  testWidgets('Test landing page', (widgetTester) async {
    await widgetTester.pumpWidget(const MaterialApp(home: LandingPage()));

    final titleFinder = find.text('Welcome to');
    final subtitleFinder = find.text('TradeBit');

    expect(titleFinder, findsOneWidget);
    expect(subtitleFinder, findsOneWidget);

    final signInButtonFinder = find.text('Sign in');
    final signUpButtonFinder = find.text('Sign up');

    expect(signInButtonFinder, findsOneWidget);
    expect(signUpButtonFinder, findsOneWidget);
  });

  testWidgets('Test landing page navigation', (widgetTester) async {
    GoRouter goRouter = GoRouter(
      initialLocation: Routes.landing.path,
      routes: [
        GoRoute(
            path: Routes.landing.path,
            builder: (context, state) => const LandingPage()),
        GoRoute(
            path: Routes.login.path,
            builder: (context, state) => const SignInPage()),
        GoRoute(
            path: Routes.register.path,
            builder: (context, state) => const SignUpPage()),
      ],
    );

    await widgetTester.pumpWidget(
      BlocsProviders(
        child: MaterialApp.router(
          routerConfig: goRouter,
        ),
      ),
    );

    final signInButtonFinder = find.text('Sign in');
    final signUpButtonFinder = find.text('Sign up');

    await widgetTester.tap(signInButtonFinder);
    await widgetTester.pumpAndSettle();
    expect(find.byType(SignInPage), findsOneWidget);

    final backButtonFinder = find.byIcon(Icons.arrow_back);

    await widgetTester.tap(backButtonFinder);
    await widgetTester.pumpAndSettle();
    await widgetTester.tap(signUpButtonFinder);
    await widgetTester.pumpAndSettle();
    expect(find.byType(SignUpPage), findsOneWidget);
  });
}
