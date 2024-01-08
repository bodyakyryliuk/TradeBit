import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/extensions/string_extensions.dart';
import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_app_bar.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/auth/domain/entities/login_response_entity.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class SignInPage extends StatefulWidget {
  const SignInPage({super.key});

  @override
  State<SignInPage> createState() => _SignInPageState();
}

class _SignInPageState extends State<SignInPage> {
  final GlobalKey<FormState> _signInFormKey = GlobalKey<FormState>();
  final emailTextEditingController = TextEditingController();
  final passwordTextEditingController = TextEditingController();

  @override
  void dispose() {
    emailTextEditingController.dispose();
    passwordTextEditingController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CommonAppBar(
        title: 'Sign in',
      ),
      body: BlocListener<LoginCubit, LoginState>(
        listener: (context, state) {
          state.whenOrNull(
            failure: (String message) {
              ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                content: Text(
                  message,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                backgroundColor: context.theme.colorScheme.error,
              ));
            },
            success: (LoginResponseEntity login) {},
          );
        },
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Form(
            key: _signInFormKey,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                const SizedBox(height: 20),
                CommonTextFormField(
                  placeholder: 'E-mail',
                  controller: emailTextEditingController,

                  validator: (String? value) {
                    if (value!.isValidEmail) {
                      return null;
                    }
                    return 'Provide correct e-mail';
                  },
                ),
                const SizedBox(height: 10),
                CommonTextFormField(
                  placeholder: 'Password',
                  obscureText: true,
                  validator: (String? value) {},
                  controller: passwordTextEditingController,
                ),
                const SizedBox(height: 15),
                CommonTextButton(
                  title: 'Sign in',
                  onPressed: () {
                    if (_signInFormKey.currentState!.validate()) {
                      context.read<LoginCubit>().login(
                          email: emailTextEditingController.text,
                          password: passwordTextEditingController.text);
                    }
                  },
                ),
                const SizedBox(height: 15),
                RichText(
                  text: TextSpan(
                    text: 'Forgot your password? ',
                    children: <TextSpan>[
                      TextSpan(
                        text: 'Reset',
                        style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Theme.of(context).primaryColor),
                        recognizer: TapGestureRecognizer()
                          ..onTap = () {
                            context.push(Routes.resetPassword.path);
                          },
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 15),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
