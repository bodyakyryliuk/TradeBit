import 'package:cointrade/core/routes/app_router.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class SkeletonPage extends StatefulWidget {
  const SkeletonPage({Key? key, required this.child}) : super(key: key);
  final Widget child;

  @override
  State<SkeletonPage> createState() => _SkeletonPageState();
}

class _SkeletonPageState extends State<SkeletonPage> {
  int _activeTabIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: widget.child,
        bottomNavigationBar: BottomNavigationBar(
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          onTap: (index) {
            setState(() {
              _activeTabIndex = index;
            });
            switch (index) {
              case 0:
                context.go(Routes.root.path);
                break;
              case 1:
                context.go(Routes.bots.path);
                break;
              case 2:
                context.go(Routes.settings.path);
                break;
            }
          },
          currentIndex: _activeTabIndex,
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.dashboard_outlined),
              label: 'Dashboard',
            ),
            BottomNavigationBarItem(
                icon: Icon(Icons.smart_toy_outlined), label: 'Bots'),
            BottomNavigationBarItem(
                icon: Icon(Icons.more_horiz), label: 'Settings'),
          ],
        ));
  }
}
