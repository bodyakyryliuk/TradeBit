import 'package:flutter/material.dart';

class CommonTextFormField extends StatelessWidget {
  const CommonTextFormField(
      {super.key,
      required this.placeholder,
      this.validator,
      this.controller,
      this.obscureText = false,
      this.readOnly = false});

  final String placeholder;
  final String? Function(String?)? validator;
  final TextEditingController? controller;
  final bool obscureText;
  final bool readOnly;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      readOnly: readOnly,
      style: const TextStyle(color: Colors.white),
      controller: controller,
      validator: validator,
      obscureText: obscureText,
      decoration: InputDecoration(
          hintText: placeholder,
          contentPadding:
              const EdgeInsets.symmetric(vertical: 20, horizontal: 20)),
    );
  }
}
