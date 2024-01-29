// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'top_up_code_response_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TopUpCode _$TopUpCodeFromJson(Map<String, dynamic> json) => TopUpCode(
      coin: json['coin'] as String,
      address: json['address'] as String,
      tag: json['tag'] as String,
      url: json['url'] as String,
      isDefault: json['isDefault'] as int,
    );

Map<String, dynamic> _$TopUpCodeToJson(TopUpCode instance) => <String, dynamic>{
      'coin': instance.coin,
      'address': instance.address,
      'tag': instance.tag,
      'url': instance.url,
      'isDefault': instance.isDefault,
    };
