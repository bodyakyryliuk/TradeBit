import 'package:flutter/material.dart';
import 'package:simple_animations/animation_builder/play_animation_builder.dart';

class EmailAnimatedIcon extends StatelessWidget {
  const EmailAnimatedIcon({super.key});

  @override
  Widget build(BuildContext context) {
    return PlayAnimationBuilder<double>(
      tween: Tween(begin: 0.4, end: 1.0),
      duration: const Duration(milliseconds: 200),
      child: const Icon(
        Icons.email_outlined,
        size: 100,
        color: Colors.white,
      ),
      builder: (context, value, child) {
        return Opacity(
            opacity: value,
            child: Transform.scale(scale: value, child: child!));
      },
    );
  }
}
