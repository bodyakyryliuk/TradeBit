import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/widgets/common_text_button.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/cubit/connect_binance_cubit.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/cubit/connect_binance_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class ConnectBinancePage extends StatefulWidget {
  const ConnectBinancePage({Key? key}) : super(key: key);

  @override
  State<ConnectBinancePage> createState() => _ConnectBinancePageState();
}

class _ConnectBinancePageState extends State<ConnectBinancePage> {
  final TextEditingController _apiKeyController = TextEditingController();
  final TextEditingController _secretApiKeyController = TextEditingController();

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  @override
  void dispose() {
    _apiKeyController.dispose();
    _secretApiKeyController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<ConnectBinanceCubit, ConnectBinanceState>(
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
          success: (LinkBinanceResponseEntity linkBinanceResponse) {},
        );
      },
      child: Scaffold(
        appBar: AppBar(title: const Text('Connect Binance')),
        floatingActionButton: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20.0),
          child: CommonTextButton(
            title: 'Connect',
            onPressed: () {
              if (_formKey.currentState!.validate()) {
                context.read<ConnectBinanceCubit>().connectBinance(
                    apiKey: _apiKeyController.text,
                    secretApiKey: _secretApiKeyController.text);
              }
            },
          ),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        body: Form(
          key: _formKey,
          child: ListView(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            children: [
              const SizedBox(height: 10),
              Text(
                'When creating API key make sure to enable trading and reading info options. Do NOT enable withdrawal option.',
                style: TextStyle(
                    color: context.theme.colorScheme.onInverseSurface
                        .withOpacity(0.8), fontSize: 16),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 20),
              CommonTextFormField(
                placeholder: 'API Key',
                controller: _apiKeyController,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Please enter a valid API Key';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                placeholder: 'Secret API Key',
                controller: _secretApiKeyController,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Please enter a valid secret API Key';
                  }
                  return null;
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
