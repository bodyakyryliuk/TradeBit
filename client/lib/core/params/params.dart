import 'package:json_annotation/json_annotation.dart';

part 'params.g.dart';

class RegisterParams {
  final String email;
  final String password;
  final String firstName;
  final String lastName;

  RegisterParams(
      {required this.firstName,
      required this.lastName,
      required this.email,
      required this.password});

  Map<String, dynamic> toJson() => {
        "email": email,
        "password": password,
        "firstname": firstName,
        "lastname": lastName,
      };
}

class LoginParams {
  final String email;
  final String password;

  LoginParams({required this.email, required this.password});

  Map<String, dynamic> toJson() => {
        "email": email,
        "password": password,
      };
}

class ResetPasswordParams {
  final String email;

  ResetPasswordParams({required this.email});

  Map<String, dynamic> toJson() => {
        "email": email,
      };
}

class RefreshTokenParams {
  final String refreshToken;

  RefreshTokenParams({required this.refreshToken});

  Map<String, dynamic> toJson() => {
        "refreshToken": refreshToken,
      };
}

class LinkBinanceParams {
  final String apiKey;
  final String secretApiKey;

  LinkBinanceParams({required this.apiKey, required this.secretApiKey});

  Map<String, dynamic> toJson() => {
        "apiKey": apiKey,
        "secretApiKey": secretApiKey,
      };
}

class TotalBalanceParams {
  final String apiKey;
  final String secretApiKey;

  TotalBalanceParams({required this.apiKey, required this.secretApiKey});

  Map<String, dynamic> toJson() => {
        "apiKey": apiKey,
        "secretApiKey": secretApiKey,
      };
}

class WalletParams {
  final String apiKey;
  final String secretApiKey;

  WalletParams({required this.apiKey, required this.secretApiKey});

  Map<String, dynamic> toJson() => {
        "apiKey": apiKey,
        "secretApiKey": secretApiKey,
      };
}

class HistoricalPricesParams {
  final String currencyPair;
  final int period;

  HistoricalPricesParams({required this.currencyPair, required this.period});
}

class CurrentPriceForTradingPairParams {
  final String tradingPair;

  CurrentPriceForTradingPairParams({required this.tradingPair});

  Map<String, dynamic> toJson() => {
        "tradingPair": tradingPair,
      };
}

@JsonSerializable(explicitToJson: true)
class MakeOrderParams {
  @JsonKey(name: "orderDTO")
  final OrderDto? orderDto;
  @JsonKey(name: "linkDTO")
  final LinkDto? linkDto;

  MakeOrderParams({
    this.orderDto,
    this.linkDto,
  });

  factory MakeOrderParams.fromJson(Map<String, dynamic> json) =>
      _$MakeOrderParamsFromJson(json);

  Map<String, dynamic> toJson() => _$MakeOrderParamsToJson(this);
}

@JsonSerializable()
class LinkDto {
  @JsonKey(name: "apiKey")
  final String? apiKey;
  @JsonKey(name: "secretApiKey")
  final String? secretApiKey;

  LinkDto({
    this.apiKey,
    this.secretApiKey,
  });

  factory LinkDto.fromJson(Map<String, dynamic> json) =>
      _$LinkDtoFromJson(json);

  Map<String, dynamic> toJson() => _$LinkDtoToJson(this);
}

@JsonSerializable()
class OrderDto {
  @JsonKey(name: "symbol")
  final String? symbol;
  @JsonKey(name: "side")
  final String? side;
  @JsonKey(name: "type")
  final String? type;
  @JsonKey(name: "quantity")
  final String? quantity;

  OrderDto({
    this.symbol,
    this.side,
    this.type,
    this.quantity,
  });

  factory OrderDto.fromJson(Map<String, dynamic> json) =>
      _$OrderDtoFromJson(json);

  Map<String, dynamic> toJson() => _$OrderDtoToJson(this);
}

@JsonSerializable()
class CreateBotParams {
  @JsonKey(name: "name")
  final String name;
  @JsonKey(name: "buyThreshold")
  final double buyThreshold;
  @JsonKey(name: "sellThreshold")
  final double sellThreshold;
  @JsonKey(name: "takeProfitPercentage")
  final double takeProfitPercentage;
  @JsonKey(name: "stopLossPercentage")
  final double stopLossPercentage;
  @JsonKey(name: "tradeSize")
  final double tradeSize;
  @JsonKey(name: "tradingPair")
  final String tradingPair;

  CreateBotParams({
    required this.name,
    required this.buyThreshold,
    required this.sellThreshold,
    required this.takeProfitPercentage,
    required this.stopLossPercentage,
    required this.tradeSize,
    required this.tradingPair,
  });

  factory CreateBotParams.fromJson(Map<String, dynamic> json) =>
      _$CreateBotParamsFromJson(json);

  Map<String, dynamic> toJson() => _$CreateBotParamsToJson(this);
}

class ToggleBotEnabledParams {
  final int botId;

  ToggleBotEnabledParams({required this.botId});

  Map<String, dynamic> toJson() => {"botId": botId};
}
