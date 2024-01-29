import 'package:cointrade/core/routes/app_router.dart';
import 'package:cointrade/core/widgets/common_app_bar.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/extensions/string_extensions.dart';
import 'package:cointrade/features/auth/domain/entities/register_response_entity.dart';
import 'package:cointrade/features/auth/domain/usecase/post_register_use_case.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class SignUpPage extends StatefulWidget {
  const SignUpPage({super.key});

  @override
  State<SignUpPage> createState() => _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final _firstNameController = TextEditingController();
  final _lastNameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _repeatPasswordController = TextEditingController();

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    _repeatPasswordController.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const CommonAppBar(
        title: 'Sign up',
      ),
      body: BlocConsumer<RegisterCubit, RegisterState>(
        listener: (_, state) {
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
            success: (RegisterResponseEntity register) {
              // ScaffoldMessenger.of(context).showSnackBar(SnackBar(
              //   content: Text(
              //     register.message,
              //     style: const TextStyle(fontWeight: FontWeight.bold),
              //   ),
              //   backgroundColor: Colors.green,
              // ));
              context.push(Routes.emailConfirmation.path,extra: _emailController.text);
            },
          );
        },
        builder: (_, state) {
          return SingleChildScrollView(
            child: IgnorePointer(
              ignoring: state == const RegisterState.loading(),
              child: AnimatedOpacity(
                opacity: state == const RegisterState.loading() ? 0.5 : 1.0,
                duration: const Duration(milliseconds: 200),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: Form(
                    key: _formKey,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        const SizedBox(height: 10),
                        CommonTextFormField(
                          placeholder: 'First name',
                          controller: _firstNameController,
                          validator: (String? value) {
                            if (value!.isValidName) {
                              return null;
                            }
                            return 'First name can not be empty';
                          },
                        ),
                        const SizedBox(height: 10),
                        CommonTextFormField(
                          placeholder: 'Last name',
                          controller: _lastNameController,
                          validator: (String? value) {
                            if (value!.isValidName) {
                              return null;
                            }
                            return 'Last name can not be empty';
                          },
                        ),
                        const SizedBox(height: 10),
                        CommonTextFormField(
                          placeholder: 'E-mail',
                          controller: _emailController,
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
                          validator: (String? value) {
                            if (value!.isNotEmpty) {
                              return null;
                            }
                            return 'Provide correct password';},
                          controller: _passwordController,
                        ),
                        const SizedBox(height: 10),
                        CommonTextFormField(
                          placeholder: 'Repeat password',
                          obscureText: true,
                          validator: (String? value) {
                            if(value!.isEmpty){
                              return 'Provide correct password';
                            }

                            if (_passwordController.text == value) {
                              return null;
                            }
                            return 'Passwords do not match';
                          },
                          controller: _repeatPasswordController,
                        ),
                        const SizedBox(height: 15),
                        CommonTextButton(
                          title: 'Sign up',
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              context.read<RegisterCubit>().register(
                                    email: _emailController.text,
                                    password: _passwordController.text,
                                    firstName: _firstNameController.text,
                                    lastName: _lastNameController.text,
                                  );
                            }
                          },
                        ),
                        const SizedBox(height: 15),
                        RichText(
                          text: TextSpan(
                            text: 'Already have an account? ',
                            children: <TextSpan>[
                              TextSpan(
                                text: 'Sign in',
                                style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    color: Theme.of(context).primaryColor),
                                recognizer: TapGestureRecognizer()
                                  ..onTap = () {
                                    context.push(Routes.login.path);
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
            ),
          );
        },
      ),
    );
  }
}
