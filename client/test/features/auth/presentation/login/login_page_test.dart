import 'package:cointrade/core/di/injector.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/login/login_page.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

main() {
  initDependencies();

  group('Test SignInPage', () {
    Future<void> pumpSignInPage(WidgetTester tester) async {
      await tester.pumpWidget(
        MaterialApp(
          home: BlocProvider<LoginCubit>(
            create: (context) => sl<LoginCubit>(), // Emit the initial state
            child: const SignInPage(),
          ),
        ),
      );
    }

    testWidgets('Test sign in page validators', (widgetTester) async {
      await pumpSignInPage(widgetTester);
      final signInButtonFinder = find.text('Sign in').first;
      await widgetTester.tap(signInButtonFinder);
      await widgetTester
          .pumpAndSettle(); // Wait for animations/transitions to complete
      await widgetTester.pump(const Duration(milliseconds: 500));
      final provideCorrectEmailFinder = find.text('Provide correct e-mail');
      expect(provideCorrectEmailFinder, findsOneWidget);
      final provideCorrectPasswordFinder =
          find.text('Provide correct password');
      expect(provideCorrectPasswordFinder, findsOneWidget);
    });
  });
}
