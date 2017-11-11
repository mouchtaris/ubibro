package incubate2

object arguments {

  trait Arguments extends Any
  trait Argument[T[_]] extends Any with Arguments
  trait NoArgument extends Any with Arguments
  trait CombinedArguments[arg1 <: Arguments, arg2 <: Arguments] extends Any with Arguments

  trait ArgumentsHandler[args <: Arguments, +in] {
    final type Args = args
  }

  trait ArgumentHandler[T[_], in] extends AnyRef
    with ArgumentsHandler[Argument[T], in]
  {
    type A
    implicit val in2a: in ⇒ A
    implicit val evidence: T[A]
  }

  trait CombinedArgumentsHandler[
    args1 <: Arguments,
    args2 <: Arguments,
    in
  ] extends AnyRef
    with ArgumentsHandler[CombinedArguments[args1, args2], in]
  {
    type in1
    type in2
    type Handler1 <: ArgumentsHandler[args1, in1]
    type Handler2 <: ArgumentsHandler[args2, in2]
    val handler1: Handler1
    val handler2: Handler2
  }

}
