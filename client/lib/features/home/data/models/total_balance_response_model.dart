import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'total_balance_response_model.g.dart';

@JsonSerializable()
class TotalBalanceResponseModel extends Equatable {
  final double? balance;
  final String? message;
  final String? status;

  const TotalBalanceResponseModel(
      {required this.balance, required this.message, required this.status});

  @override
  List<Object?> get props => [balance, message, status];

  factory TotalBalanceResponseModel.fromJson(Map<String, dynamic> json) =>
      _$TotalBalanceResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$TotalBalanceResponseModelToJson(this);
}
