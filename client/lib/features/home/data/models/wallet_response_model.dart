import 'package:json_annotation/json_annotation.dart';

part 'wallet_response_model.g.dart';

@JsonSerializable()
class WalletResponseModel {
  @JsonKey(name: "balances")
  final List<Balance>? balances;
  @JsonKey(name: "canTrade")
  final bool? canTrade;
  @JsonKey(name: "canWithdraw")
  final bool? canWithdraw;
  @JsonKey(name: "canDeposit")
  final bool? canDeposit;

  final String? message;
  final String? status;

  WalletResponseModel( {
    this.balances,
    this.canTrade,
    this.canWithdraw,
    this.canDeposit,
    this.message, this.status,
  });

  factory WalletResponseModel.fromJson(Map<String, dynamic> json) => _$WalletResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$WalletResponseModelToJson(this);
}

@JsonSerializable()
class Balance {
  @JsonKey(name: "asset")
  final String? asset;
  @JsonKey(name: "free")
  final double? free;
  @JsonKey(name: "locked")
  final double? locked;
  @JsonKey(name: "priceChange")
  final double? priceChange;

  Balance({
    this.asset,
    this.free,
    this.locked,
    this.priceChange,
  });

  factory Balance.fromJson(Map<String, dynamic> json) => _$BalanceFromJson(json);

  Map<String, dynamic> toJson() => _$BalanceToJson(this);
}
