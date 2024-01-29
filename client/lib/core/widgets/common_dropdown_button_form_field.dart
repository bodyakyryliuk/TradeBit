import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:flutter/material.dart';

class CommonDropdownButtonFormField extends StatelessWidget {
  const CommonDropdownButtonFormField(
      {Key? key,
      required this.onChanged,
      this.value,
      required this.items,
      this.hint,
      this.validator,
      this.icon})
      : super(key: key);

  final Function(String?) onChanged;
  final String? value;

  final List<String> items;
  final String? hint;

  final String? Function(String?)? validator;

  final Widget? icon;

  @override
  Widget build(BuildContext context) {
    return DropdownButtonFormField<String>(
      icon: icon,
      validator: validator,
      value: value,
      items: items
          .map((e) => DropdownMenuItem<String>(
                value: e,
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: Text(e),
                ),
              ))
          .toList(),
      onChanged: onChanged,
      hint: hint != null
          ? Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8),
              child: Text(
                hint!,
                style: const TextStyle(color: Colors.white70),
              ),
            )
          : null,
    );
  }
}
