import 'package:json_annotation/json_annotation.dart';

part 'make_order_response_model.g.dart';

@JsonSerializable()
class MakeOrderResponseModel {

  final String? message;

  @JsonKey(name: "symbol")
  final String? symbol;
  @JsonKey(name: "orderId")
  final int? orderId;
  @JsonKey(name: "orderListId")
  final int? orderListId;
  @JsonKey(name: "clientOrderId")
  final String? clientOrderId;
  @JsonKey(name: "transactTime")
  final int? transactTime;
  @JsonKey(name: "price")
  final String? price;
  @JsonKey(name: "origQty")
  final String? origQty;
  @JsonKey(name: "executedQty")
  final String? executedQty;
  @JsonKey(name: "cummulativeQuoteQty")
  final String? cummulativeQuoteQty;
  @JsonKey(name: "status")
  final String? status;
  @JsonKey(name: "timeInForce")
  final String? timeInForce;
  @JsonKey(name: "type")
  final String? type;
  @JsonKey(name: "side")
  final String? side;
  @JsonKey(name: "workingTime")
  final int? workingTime;
  @JsonKey(name: "fills")
  final List<Fill>? fills;
  @JsonKey(name: "selfTradePreventionMode")
  final String? selfTradePreventionMode;

  MakeOrderResponseModel({
    this.message,
    this.symbol,
    this.orderId,
    this.orderListId,
    this.clientOrderId,
    this.transactTime,
    this.price,
    this.origQty,
    this.executedQty,
    this.cummulativeQuoteQty,
    this.status,
    this.timeInForce,
    this.type,
    this.side,
    this.workingTime,
    this.fills,
    this.selfTradePreventionMode,
  });

  factory MakeOrderResponseModel.fromJson(Map<String, dynamic> json) => _$MakeOrderResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$MakeOrderResponseModelToJson(this);
}

@JsonSerializable()
class Fill {
  @JsonKey(name: "price")
  final String? price;
  @JsonKey(name: "qty")
  final String? qty;
  @JsonKey(name: "commission")
  final String? commission;
  @JsonKey(name: "commissionAsset")
  final String? commissionAsset;
  @JsonKey(name: "tradeId")
  final int? tradeId;

  Fill({
    this.price,
    this.qty,
    this.commission,
    this.commissionAsset,
    this.tradeId,
  });

  factory Fill.fromJson(Map<String, dynamic> json) => _$FillFromJson(json);

  Map<String, dynamic> toJson() => _$FillToJson(this);
}
