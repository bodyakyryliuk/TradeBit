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
