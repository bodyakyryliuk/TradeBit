import 'package:flutter/material.dart';

ThemeData darkTheme(BuildContext context) {
  return ThemeData.dark().copyWith(
    colorScheme: ColorScheme.fromSeed(
      seedColor: Colors.deepPurple,
      error: Colors.red,
    ),
    primaryColor: Colors.deepPurple,
    useMaterial3: true,
    scaffoldBackgroundColor: Colors.black,
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: Colors.grey.withOpacity(0.12),
      hintStyle: const TextStyle(color: Colors.white70),
      labelStyle: const TextStyle(color: Colors.white),
      focusedErrorBorder: OutlineInputBorder(
          borderSide: BorderSide(width: 2, color: Colors.red[900]!),
          borderRadius: BorderRadius.circular(16)),
      errorBorder: OutlineInputBorder(
          borderSide: BorderSide(width: 2, color: Colors.red[900]!),
          borderRadius: BorderRadius.circular(16)),
      enabledBorder: OutlineInputBorder(
          borderSide:
              BorderSide(width: 2, color: Colors.grey[800]!.withOpacity(0.5)),
          borderRadius: BorderRadius.circular(16)),
      focusedBorder: OutlineInputBorder(
          borderSide: const BorderSide(width: 2, color: Colors.deepPurple),
          borderRadius: BorderRadius.circular(16)),
    ),
    textButtonTheme: TextButtonThemeData(
      style: TextButton.styleFrom(
        backgroundColor: Colors.deepPurple,
        textStyle: const TextStyle(color: Colors.white),
      ),
    ),
  );
}
