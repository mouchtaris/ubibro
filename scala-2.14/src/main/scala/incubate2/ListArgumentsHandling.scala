package incubate2

import
  list._,
  arguments.{
    ArgumentsHandler ⇒ ArgumentsHandlerT,
    ArgumentHandler ⇒ ArgumentHandlerT,
    CombinedArgumentsHandler ⇒ CombinedArgumentsHandlerT,
    _
  }

object ListArgumentsHandling {

  trait ListArgumentsHandler[rest <: List] extends AnyRef
  {
    this: ArgumentsHandlerT[_, _] ⇒

    final type Remainder = rest
    val consume: In ⇒ Remainder
  }

  case class ArgumentsHandler[args <: Arguments, rest <: List, in <: List](
    consume: in ⇒ rest
  ) extends AnyRef
    with ListArgumentsHandler[rest]
    with ArgumentsHandlerT[args, in]

  case class ArgumentHandler[
    T[_],
    a: T,
    rest <: List,
    in <: List
  ](
    in2a: in ⇒ a,
    consume: in ⇒ rest
  ) extends AnyRef
    with ListArgumentsHandler[rest]
    with ArgumentHandlerT[T, in]
  {
    type A = a
    val evidence: T[A] = implicitly
  }

  case class CombinedArgumentsHandler[
    args1 <: Arguments,
    han1 <: ArgumentsHandlerT[args1, in],
    args2 <: Arguments,
    han2 <: ArgumentsHandlerT[args2, in],
    rest <: List,
    in <: List
  ](
    handler1: han1,
    handler2: han2,
    consume: in ⇒ rest
  ) extends AnyRef
    with ListArgumentsHandler[rest]
    with CombinedArgumentsHandlerT[args1, han1, args2, han2, in]

  implicit def listArgumentHandler[T[_], h: T, t <: List]: ArgumentHandler[T, h, t, h :: t] =
    ArgumentHandler(_.head, _.tail)

  implicit def listCombinedArgumentHandler[
    args1 <: Arguments,
    args2 <: Arguments,
    rest1 <: List,
    rest2 <: List,
    in <: List
  ](
    implicit
    han1: ArgumentsHandlerT[args1, in] with ListArgumentsHandler[rest1],
    han2: ArgumentsHandlerT[args2, rest1] with ListArgumentsHandler[rest2]
  ): CombinedArgumentsHandler[args1, han1.type, args2, ArgumentsHandler[args2, rest2, in], rest2, in] = {
    val superconsumer: in ⇒ rest2 = han1.consume andThen han2.consume
    CombinedArgumentsHandler(
      han1,
      ArgumentsHandler(superconsumer),
      superconsumer
    )
  }

}
