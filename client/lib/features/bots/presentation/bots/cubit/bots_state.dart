part of 'bots_cubit.dart';

class BotsState {
  final List<BotModel> bots;

  final bool requested;
  final String? errorMessage;

  BotsState({this.bots = const [], this.errorMessage, this.requested = false});

  BotsState copyWith({
    List<BotModel>? bots,
    bool? requested,
    String? message,
  }) {
    return BotsState(
      bots: bots ?? this.bots,
      requested: requested ?? this.requested,
      errorMessage: message ?? this.errorMessage,
    );
  }
}
