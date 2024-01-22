import 'package:flutter/material.dart';

class CommonTextButton extends StatelessWidget {
  const CommonTextButton({
    super.key,
    required this.title,
    this.onPressed,
    this.isPrimary = true,
    this.color,
  });

  final String title;
  final VoidCallback? onPressed;

  final bool isPrimary;

  final Color? color;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      style: TextButton.styleFrom(
          foregroundColor: Theme.of(context).colorScheme.background,
          backgroundColor: color ??
              (isPrimary
                  ? Theme.of(context).colorScheme.primary
                  : Colors.grey[900]),
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          padding: const EdgeInsets.all(18)),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            title,
            style: const TextStyle(
                color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
          ),
        ],
      ),
    );
  }
}
