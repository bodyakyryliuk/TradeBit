import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:flutter/material.dart';

class NotFoundPage extends StatelessWidget {
  const NotFoundPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Text(
          'Page not found',
          style: TextStyle(color: context.theme.primaryColor,fontSize: 20),
        ),
      ),
    );
  }
}
