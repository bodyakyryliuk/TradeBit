import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/features/auth/presentation/email_verification/components/email_animated_icon.dart';
import 'package:cointrade/features/auth/presentation/email_verification/components/email_verification_letter_animated_title.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class EmailConfirmationPage extends StatelessWidget {
  const EmailConfirmationPage({super.key, required this.email});

  final String email;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Spacer(),
            const EmailAnimatedIcon(),
            const SizedBox(height: 10),
            EmailVerificationLetterAnimatedTitle(email: email),
            const Spacer(),
            CommonTextButton(title: 'Login', onPressed: () {
              context.push(Routes.login.path);
            }),
            const SizedBox(height: 10),
          ],
        ),
      ),
    );
  }
}
