// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'make_order_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MakeOrderResponseModel _$MakeOrderResponseModelFromJson(
        Map<String, dynamic> json) =>
    MakeOrderResponseModel(
      message: json['message'] as String?,
      symbol: json['symbol'] as String?,
      orderId: json['orderId'] as int?,
      orderListId: json['orderListId'] as int?,
      clientOrderId: json['clientOrderId'] as String?,
      transactTime: json['transactTime'] as int?,
      price: json['price'] as String?,
      origQty: json['origQty'] as String?,
      executedQty: json['executedQty'] as String?,
      cummulativeQuoteQty: json['cummulativeQuoteQty'] as String?,
      status: json['status'] as String?,
      timeInForce: json['timeInForce'] as String?,
      type: json['type'] as String?,
      side: json['side'] as String?,
      workingTime: json['workingTime'] as int?,
      fills: (json['fills'] as List<dynamic>?)
          ?.map((e) => Fill.fromJson(e as Map<String, dynamic>))
          .toList(),
      selfTradePreventionMode: json['selfTradePreventionMode'] as String?,
    );

Map<String, dynamic> _$MakeOrderResponseModelToJson(
        MakeOrderResponseModel instance) =>
    <String, dynamic>{
      'message': instance.message,
      'symbol': instance.symbol,
      'orderId': instance.orderId,
      'orderListId': instance.orderListId,
      'clientOrderId': instance.clientOrderId,
      'transactTime': instance.transactTime,
      'price': instance.price,
      'origQty': instance.origQty,
      'executedQty': instance.executedQty,
      'cummulativeQuoteQty': instance.cummulativeQuoteQty,
      'status': instance.status,
      'timeInForce': instance.timeInForce,
      'type': instance.type,
      'side': instance.side,
      'workingTime': instance.workingTime,
      'fills': instance.fills,
      'selfTradePreventionMode': instance.selfTradePreventionMode,
    };

Fill _$FillFromJson(Map<String, dynamic> json) => Fill(
      price: json['price'] as String?,
      qty: json['qty'] as String?,
      commission: json['commission'] as String?,
      commissionAsset: json['commissionAsset'] as String?,
      tradeId: json['tradeId'] as int?,
    );

Map<String, dynamic> _$FillToJson(Fill instance) => <String, dynamic>{
      'price': instance.price,
      'qty': instance.qty,
      'commission': instance.commission,
      'commissionAsset': instance.commissionAsset,
      'tradeId': instance.tradeId,
    };
