import 'package:cointrade/core/extensions/build_context_extensions.dart';
import 'package:cointrade/core/utils/logger.dart';
import 'package:cointrade/features/settings/data/models/top_up_code_response_model.dart';
import 'package:cointrade/features/settings/presentation/top_up/cubit/top_up_code_cubit.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:qr_flutter/qr_flutter.dart';

class TopUpPage extends StatefulWidget {
  const TopUpPage({Key? key}) : super(key: key);

  @override
  _TopUpPageState createState() => _TopUpPageState();
}

class _TopUpPageState extends State<TopUpPage> {
  @override
  void initState() {
    context.read<TopUpCodeCubit>().fetchTopUpCode(coin: _selectedCoin);
    super.initState();
  }

  String _selectedCoin = 'USDT';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Top up'),
      ),
      body: Column(
        children: [

          const SizedBox(height: 20),
          CupertinoSlidingSegmentedControl(
              thumbColor: Colors.grey[900]!,
              backgroundColor: CupertinoColors.darkBackgroundGray,
              children: const {
                'USDT': Text('USDT'),
                'BTC': Text('BTC'),
                'ETH': Text('ETH'),
                'BNB': Text('BNB'),
              },
              groupValue: _selectedCoin,
              onValueChanged: (String? value) {
                setState(() {
                  _selectedCoin = value!;
                  context
                      .read<TopUpCodeCubit>()
                      .fetchTopUpCode(coin: _selectedCoin);
                });
              }),
          const SizedBox(height: 20),
          Expanded(
            child: BlocBuilder<TopUpCodeCubit, TopUpCodeState>(
              builder: (context, state) {
                return state.when(
                  initial: () => const SizedBox(),
                  loading: () => const Center(
                    child: CircularProgressIndicator(),
                  ),
                  success: (TopUpCodeResponseModel topUpCodeResponseModel) {
                    return Center(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 20.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Spacer(),
                            ClipRRect(
                              borderRadius: BorderRadius.circular(20),
                              child: QrImageView(
                                data: topUpCodeResponseModel.topUpCode!.url,
                                version: QrVersions.auto,
                                padding: const EdgeInsets.all(20),
                                size: 300.0,
                                backgroundColor: Colors.white,
                              ),
                            ),
                            Spacer(),
                            Container(
                              padding: const EdgeInsets.all(20),
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(20),
                                color: Colors.grey[900],
                              ),
                              child: Row(
                                children: [
                                  Expanded(
                                    child: Text(
                                      topUpCodeResponseModel.topUpCode!.url,
                                      style: const TextStyle(
                                        fontSize: 18,
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                  ),
                                  IconButton(
                                    onPressed: () {
                                      Clipboard.setData(
                                        ClipboardData(
                                          text: topUpCodeResponseModel
                                              .topUpCode!.url,
                                        ),
                                      );
                                    },
                                    icon: Icon(
                                      Icons.copy,
                                      color: context.theme.primaryColor,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            Spacer(),
                          ],
                        ),
                      ),
                    );
                  },
                  failure: (message) => Center(
                    child: Text(message),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
