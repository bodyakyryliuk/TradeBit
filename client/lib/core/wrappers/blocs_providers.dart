import 'package:cointrade/core/di/injector.dart';
import 'package:cointrade/features/auth/presentation/login/cubit/login_cubit.dart';
import 'package:cointrade/features/auth/presentation/register/cubit/register_cubit.dart';
import 'package:cointrade/features/auth/presentation/reset_password/cubit/reset_password_cubit.dart';
import 'package:cointrade/features/home/domain/usecases/fetch_total_balance_use_case.dart';
import 'package:cointrade/features/home/presentation/total_balance/cubit/total_balance_cubit.dart';
import 'package:cointrade/features/settings/presentation/connect_binance/cubit/connect_binance_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BlocsProviders extends StatelessWidget {
  const BlocsProviders({super.key, required this.child});
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: <BlocProvider>[
        BlocProvider<RegisterCubit>(create: (_) => sl<RegisterCubit>()),
        BlocProvider<LoginCubit>(create: (_) => sl<LoginCubit>()),
        BlocProvider<ResetPasswordCubit>(create: (_) => sl<ResetPasswordCubit>()),
        BlocProvider<ConnectBinanceCubit>(create: (_) => sl<ConnectBinanceCubit>()),
        BlocProvider<TotalBalanceCubit>(create: (_) => sl<TotalBalanceCubit>()),
      ],
      child: child,
    );
  }
}
