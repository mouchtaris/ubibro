package incubate2

object arguments {

  trait Arguments extends Any
  trait Argument[T[_]] extends Any with Arguments
  trait NoArgument extends Any with Arguments
  trait CombinedArguments[arg1 <: Arguments, arg2 <: Arguments] extends Any with Arguments

  trait ArgumentsHandler[args <: Arguments, in] {
    final type Args = args
    final type In = in
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
    han1 <: ArgumentsHandler[args1, in],
    args2 <: Arguments,
    han2 <: ArgumentsHandler[args2, in],
    in
  ] extends AnyRef
    with ArgumentsHandler[CombinedArguments[args1, args2], in]
  {
    def handler1: han1
    def handler2: han2
  }

}
