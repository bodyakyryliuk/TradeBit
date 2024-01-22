import 'package:flutter/material.dart';

class CommonTextFormField extends StatelessWidget {
  const CommonTextFormField({
    super.key,
    this.placeholder,
    this.validator,
    this.controller,
    this.obscureText = false,
    this.readOnly = false,
    this.keyboardType,
    this.prefix,
    this.prefixText,
    this.initialValue,
    this.prefixIcon,
    this.focusNode,
  });

  final String? placeholder;
  final String? Function(String?)? validator;
  final TextEditingController? controller;
  final bool obscureText;
  final bool readOnly;
  final TextInputType? keyboardType;
  final Widget? prefix;
  final String? prefixText;
  final Widget? prefixIcon;
  final String? initialValue;
  final FocusNode? focusNode;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      readOnly: readOnly,
      focusNode: focusNode,
      initialValue: initialValue,
      style: const TextStyle(color: Colors.white),
      controller: controller,
      validator: validator,
      obscureText: obscureText,
      keyboardType: keyboardType,
      decoration: InputDecoration(
          hintText: placeholder,
          prefix: prefix,
          prefixText: prefixText,
          prefixIcon: prefixIcon,
          contentPadding:
              const EdgeInsets.symmetric(vertical: 20, horizontal: 20)),
    );
  }
}
