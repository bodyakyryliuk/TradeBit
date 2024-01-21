// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'wallet_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

WalletResponseModel _$WalletResponseModelFromJson(Map<String, dynamic> json) =>
    WalletResponseModel(
      balances: (json['balances'] as List<dynamic>?)
          ?.map((e) => Balance.fromJson(e as Map<String, dynamic>))
          .toList(),
      canTrade: json['canTrade'] as bool?,
      canWithdraw: json['canWithdraw'] as bool?,
      canDeposit: json['canDeposit'] as bool?,
      message: json['message'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$WalletResponseModelToJson(
        WalletResponseModel instance) =>
    <String, dynamic>{
      'balances': instance.balances,
      'canTrade': instance.canTrade,
      'canWithdraw': instance.canWithdraw,
      'canDeposit': instance.canDeposit,
      'message': instance.message,
      'status': instance.status,
    };

Balance _$BalanceFromJson(Map<String, dynamic> json) => Balance(
      asset: json['asset'] as String?,
      free: (json['free'] as num?)?.toDouble(),
      locked: (json['locked'] as num?)?.toDouble(),
      priceChange: (json['priceChange'] as num?)?.toDouble(),
    );

Map<String, dynamic> _$BalanceToJson(Balance instance) => <String, dynamic>{
      'asset': instance.asset,
      'free': instance.free,
      'locked': instance.locked,
      'priceChange': instance.priceChange,
    };
