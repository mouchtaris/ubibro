package incubate2

import
  list._,
  arguments._

object ListArgumentHandling {

  trait ListArgumentsHandler[rest <: List, in <: List] extends AnyRef
  {
    this: ArgumentsHandler[_, in] ⇒

    final type Remainder = rest
    val consume: in ⇒ Remainder
  }

  case class ListArgumentHandler[
    T[_],
    a: T,
    rest <: List,
    in <: List
  ](
    in2a: in ⇒ a,
    consume: in ⇒ rest
  ) extends AnyRef
    with ListArgumentsHandler[rest, in]
    with ArgumentHandler[T, in]
  {
    type A = a
    val evidence: T[A] = implicitly
  }

  case class ListCombinedArgumentsHandler[
    args1 <: Arguments,
    args2 <: Arguments,
    rest1 <: List,
    rest2 <: List,
    handler1 <: ArgumentsHandler[args1, in] with ListArgumentsHandler[rest1, in],
    handler2 <: ArgumentsHandler[args2, rest1] with ListArgumentsHandler[rest2, rest1],
    in <: List
  ](
    handler1: handler1,
    handler2: handler2,
    consume: in ⇒ rest2
  ) extends AnyRef
    with ListArgumentsHandler[rest2, in]
    with CombinedArgumentsHandler[args1, args2, in]
  {
    type in1 = in
    type in2 = rest1
    type Handler1 = handler1
    type Handler2 = handler2
  }

  implicit def listArgumentHandler[T[_], h: T, t <: List]: ListArgumentHandler[T, h, t, h :: t] =
    ListArgumentHandler(_.head, _.tail)

  implicit def combineArgsAndArgument[
    args1 <: Arguments,
    args2 <: Arguments,
    rest1 <: List,
    rest2 <: List,
    in <: List
  ](
    implicit dummy: DummyImplicit,
    handler1: ArgumentsHandler[args1, in] with ListArgumentsHandler[rest1, in],
    handler2: ArgumentsHandler[args2, rest1] with ListArgumentsHandler[rest2, rest1]
  ):
    ListCombinedArgumentsHandler[
      args1, args2, rest1, rest2,
      handler1.type, handler2.type,
      in
    ]
  =
    ListCombinedArgumentsHandler(
      handler1,
      handler2,
      handler1.consume andThen handler2.consume
    )

  //implicit def listCombinedArgumentHandler[
  //  args1 <: Arguments,
  //  args2 <: Arguments,
  //  rest1 <: List,
  //  rest2 <: List,
  //  in <: List
  //](
  //  implicit
  //  han1: ArgumentsHandler[args1, in] with ListArgumentsHandler[rest1],
  //  han2: ArgumentsHandler[args2, rest1] with ListArgumentsHandler[rest2]
  //): ListCombinedArgumentsHandlerImpl[
  //    args1, han1.type,
  //    args2, ListArgumentsHandlerImpl[args2, rest2, in],
  //    rest2, in
  //] = {
  //  val superconsumer: in ⇒ rest2 = han1.consume andThen han2.consume
  //  ListCombinedArgumentsHandlerImpl(
  //    han1,
  //    ListArgumentsHandlerImpl(superconsumer),
  //    superconsumer
  //  )
  //}

}
