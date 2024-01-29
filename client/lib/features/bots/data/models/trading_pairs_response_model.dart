class TradingPairsResponseModel{
  final List<String> tradingPairs;

  TradingPairsResponseModel({required this.tradingPairs});

  factory TradingPairsResponseModel.fromJson(List<dynamic> json) {
    final List<String> tradingPairs = List<String>.from(json);

    return TradingPairsResponseModel(tradingPairs: tradingPairs);
  }

}