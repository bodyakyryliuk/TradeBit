import 'package:equatable/equatable.dart';

class LinkBinanceResponseEntity extends Equatable {
  final String? message;
  final String? error;

  const LinkBinanceResponseEntity( { this.message,  this.error});

  @override
  List<Object?> get props => [message, error];
}
