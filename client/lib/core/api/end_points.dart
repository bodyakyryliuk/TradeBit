class EndPoints {
  const EndPoints._();

  static const String baseUrl = 'http://10.0.2.2:8080';
  static const String register = "/identity-service/auth/register";
  static const String login = "/identity-service/auth/login/email";
  static const String resetPassword = "/identity-service/auth/forgot-password";

  static const String linkBinance = "/binance-service/account/link-binance";
}
