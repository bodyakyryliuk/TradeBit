import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/utils/logger.dart';
import 'package:cointrade/core/widgets/common_text_form_field.dart';
import 'package:cointrade/features/bots/presentation/add_bot/cubit/add_bot_cubit.dart';
import 'package:cointrade/features/bots/presentation/bots/cubit/bots_cubit.dart';
import 'package:cointrade/core/widgets/common_dropdown_button_form_field.dart';
import 'package:cointrade/features/bots/presentation/components/all_trading_pairs/cubit/all_trading_pairs_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';

class AddBotPage extends StatefulWidget {
  const AddBotPage({Key? key}) : super(key: key);

  @override
  State<AddBotPage> createState() => _AddBotPageState();
}

class _AddBotPageState extends State<AddBotPage> {
  final formKey = GlobalKey<FormState>();
  final nameController = TextEditingController();
  final buyThresholdController = TextEditingController();
  final sellThresholdController = TextEditingController();
  final takeProfitPercentageController = TextEditingController();
  final stopLossPercentageController = TextEditingController();
  final tradeSizeController = TextEditingController();

  String? tradingPair;

  @override
  void initState() {
    context.read<AllTradingPairsCubit>().fetchTradingPairs();
    super.initState();
  }

  @override
  void dispose() {
    nameController.dispose();
    buyThresholdController.dispose();
    sellThresholdController.dispose();
    takeProfitPercentageController.dispose();
    stopLossPercentageController.dispose();
    tradeSizeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AddBotCubit, AddBotState>(
      listener: (context, state) {
        state.whenOrNull(
          success: (createBotResponseModel) {
            context.read<BotsCubit>().fetchBots();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(
                    "Bot \"${createBotResponseModel.name!}\" created successfully"),
                backgroundColor: Colors.green,
              ),
            );
            context.pop();
          },
          failure: (message) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(message),
                backgroundColor: Colors.red,
              ),
            );
          },
        );
      },
      child: Scaffold(
        appBar: AppBar(title: const Text('Add bot'), actions: [
          IconButton(
            onPressed: () {
              if (formKey.currentState!.validate()) {
                context.read<AddBotCubit>().createBot(
                      name: nameController.text,
                      buyThreshold: double.parse(buyThresholdController.text),
                      sellThreshold: double.parse(sellThresholdController.text),
                      takeProfitPercentage:
                          double.parse(takeProfitPercentageController.text),
                      stopLossPercentage:
                          double.parse(stopLossPercentageController.text),
                      tradeSize: double.parse(tradeSizeController.text),
                      tradingPair: tradingPair!,
                    );
              }
            },
            icon: Icon(
              Icons.check,
              color: context.theme.primaryColor,
            ),
          )
        ]),
        body: Form(
          key: formKey,
          child: ListView(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            children: [
              CommonTextFormField(
                placeholder: 'Name',
                controller: nameController,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Name must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                controller: buyThresholdController,
                placeholder: 'Buy threshold',
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Buy threshold must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                controller: sellThresholdController,
                placeholder: 'Sell threshold',
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Sell threshold must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                controller: takeProfitPercentageController,
                placeholder: 'Take profit percentage',
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Take profit percentage must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                controller: stopLossPercentageController,
                placeholder: 'Stop loss percentage',
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Stop loss percentage must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              CommonTextFormField(
                controller: tradeSizeController,
                placeholder: 'Trade Size',
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Trade size must not be empty';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 15),
              BlocBuilder<AllTradingPairsCubit, AllTradingPairsState>(
                builder: (context, AllTradingPairsState state) {
                  print(state);
                  List<String> items = state.when(
                    initial: () => [],
                    loading: () => [],
                    success: (tradingPairsResponseModel) =>
                        tradingPairsResponseModel.tradingPairs,
                    failure: (message) => [],
                  );

                  bool isLoading =
                      state.maybeWhen(loading: () => true, orElse: () => false);

                  return CommonDropdownButtonFormField(
                    icon: isLoading
                        ? const SizedBox(
                            height: 15,
                            width: 15,
                            child: CircularProgressIndicator())
                        : null,
                    hint: 'Trading pair',
                    items: items,
                    validator: (value) {
                      if (value == null ||
                          value.isEmpty ||
                          tradingPair == null) {
                        return 'Trading pair must not be empty';
                      }
                      return null;
                    },
                    onChanged: (String? value) {
                      setState(() {
                        tradingPair = value!;
                      });
                    },
                    value: tradingPair,
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
