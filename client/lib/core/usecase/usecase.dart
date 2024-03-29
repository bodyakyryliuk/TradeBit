import 'package:cointrade/core/error/failures.dart';
import 'package:dartz/dartz.dart';
import 'package:equatable/equatable.dart';

abstract class UseCase<Type, Params>{
  Future<Either<Failure, Type>> call(Params params);
}
abstract class SyncUseCase<Type, Params>{
  Either<Failure, Type> call(Params params);
}
class NoParams extends Equatable{
  @override
  List<Object?> get props => [""];
}