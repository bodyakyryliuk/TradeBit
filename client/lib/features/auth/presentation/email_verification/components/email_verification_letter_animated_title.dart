import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:flutter/material.dart';
import 'package:simple_animations/simple_animations.dart';

class EmailVerificationLetterAnimatedTitle extends StatefulWidget {
  const EmailVerificationLetterAnimatedTitle({super.key, required this.email});

  final String email;

  @override
  State<EmailVerificationLetterAnimatedTitle> createState() =>
      _EmailVerificationLetterAnimatedTitleState();
}

class _EmailVerificationLetterAnimatedTitleState
    extends State<EmailVerificationLetterAnimatedTitle> {
  @override
  Widget build(BuildContext context) {
    final tween = MovieTween()
      ..tween('yTranslate', Tween<double>(begin: -20.0, end: 0.0),
          duration: const Duration(milliseconds: 700))
      ..tween('opacity', Tween<double>(begin: 0.0, end: 1.0),
          duration: const Duration(milliseconds: 200));

    return PlayAnimationBuilder<Movie>(
      tween: tween,
      duration: tween.duration,
      delay: const Duration(milliseconds: 200),
      child: RichText(
        text: TextSpan(
          text: 'Email verification letter was sent to ',
          children: <TextSpan>[
            TextSpan(
                text: widget.email,
                style: TextStyle(
                    fontWeight: FontWeight.bold,
                    color: context.theme.colorScheme.primary)),
          ],
          style:
              const TextStyle(color: Colors.white, fontSize: 20, height: 1.4),
        ),
        textAlign: TextAlign.center,
      ),
      builder: (context, value, child) {
        return Transform.translate(
            offset: Offset(0, value.get('yTranslate')),
            child: Opacity(
              opacity: value.get('opacity'),
              child: child!,
            ));
      },
    );
  }
}
