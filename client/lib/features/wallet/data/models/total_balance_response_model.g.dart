// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'total_balance_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TotalBalanceResponseModel _$TotalBalanceResponseModelFromJson(
        Map<String, dynamic> json) =>
    TotalBalanceResponseModel(
      balance: (json['balance'] as num?)?.toDouble(),
      message: json['message'] as String?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$TotalBalanceResponseModelToJson(
        TotalBalanceResponseModel instance) =>
    <String, dynamic>{
      'balance': instance.balance,
      'message': instance.message,
      'status': instance.status,
    };
