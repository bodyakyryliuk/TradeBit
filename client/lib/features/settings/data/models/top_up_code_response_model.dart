import 'package:json_annotation/json_annotation.dart';

part 'top_up_code_response_model.g.dart';

class TopUpCodeResponseModel {
  final String? status;
  final String? message;

  final TopUpCode? topUpCode;

  TopUpCodeResponseModel(
      {required this.status, required this.message, required this.topUpCode});

  factory TopUpCodeResponseModel.fromJson(Map<String, dynamic> json) {
    return TopUpCodeResponseModel(
      status: json['status'],
      message: json['message'],
      topUpCode: TopUpCode.fromJson(json),
    );
  }
}

@JsonSerializable()
class TopUpCode {
  @JsonKey(name: "coin")
  final String coin;
  @JsonKey(name: "address")
  final String address;
  @JsonKey(name: "tag")
  final String tag;
  @JsonKey(name: "url")
  final String url;
  @JsonKey(name: "isDefault")
  final int isDefault;

  TopUpCode({
    required this.coin,
    required this.address,
    required this.tag,
    required this.url,
    required this.isDefault,
  });

  factory TopUpCode.fromJson(Map<String, dynamic> json) =>
      _$TopUpCodeFromJson(json);

  Map<String, dynamic> toJson() => _$TopUpCodeToJson(this);
}
