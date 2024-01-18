import 'package:cointrade/features/settings/domain/entities/link_binance_response_entity.dart';
import 'package:equatable/equatable.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'link_binance_response_model.g.dart';

@JsonSerializable()
class LinkBinanceResponseModel extends Equatable {
  final String? message;
  final String? status;

  final String? error;

  const LinkBinanceResponseModel({this.message, this.status, this.error});

  @override
  List<Object?> get props => [message, status, error];

  LinkBinanceResponseEntity toEntity() =>
      LinkBinanceResponseEntity(message: message, error: error);

  factory LinkBinanceResponseModel.fromJson(Map<String, dynamic> json) =>
      _$LinkBinanceResponseModelFromJson(json);

  Map<String, dynamic> toJson() => _$LinkBinanceResponseModelToJson(this);
}
