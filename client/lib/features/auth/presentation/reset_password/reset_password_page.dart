import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/extensions/string_extensions.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/auth/domain/entities/reset_password_response_entity.dart';
import 'package:cointrade/features/auth/presentation/reset_password/cubit/reset_password_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class ResetPasswordPage extends StatefulWidget {
  const ResetPasswordPage({Key? key}) : super(key: key);

  @override
  _ResetPasswordPageState createState() => _ResetPasswordPageState();
}

class _ResetPasswordPageState extends State<ResetPasswordPage> {
  final TextEditingController _emailController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  @override
  void dispose() {
    _emailController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<ResetPasswordCubit, ResetPasswordState>(
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
          success: (ResetPasswordResponseEntity resetPasswordResponseEntity) {
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
              content: Text(
                resetPasswordResponseEntity.success!,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              backgroundColor: Colors.green,
            ));
          },
        );
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Reset password'),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20.0),
          child: CommonTextButton(
              title: 'Reset password',
              onPressed: () {
                if (_formKey.currentState!.validate()) {
                  context
                      .read<ResetPasswordCubit>()
                      .resetPassword(email: _emailController.text);
                }
              }),
        ),
        body: Form(
          key: _formKey,
          child: ListView(
            padding: const EdgeInsets.symmetric(horizontal: 20.0),
            children: [
              CommonTextFormField(
                placeholder: 'Email',
                controller: _emailController,
                validator: (String? value) {
                  if (value!.isValidEmail) {
                    return null;
                  }
                  return 'Provide correct e-mail';
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
