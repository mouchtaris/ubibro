import scala.language.strictEquality
import typeops._
import pig._
import core._

object listarguments {

  trait ListArguments[in] extends Arguments[in] {
    type Args <: List
  }

  case class NoListArguments[in]()
    extends ListArguments[in]
    with NoArguments[in]
  {
    type Args = Nil
  }
  trait NoListArgumentsInferences extends Any {
    inline implicit def noArgs[in <: List]: NoListArguments[in] = 
      NoListArguments()
  }
  object NoListArguments extends AnyRef
    with NoListArgumentsInferences

  case class ListArgument[h, in](
  )(
    implicit
    ev: in <:< h :: List
  ) extends ListArguments[in]
    with Argument[h, in]
  {
    type Args = h :: List
    val a: Read[in, h] = implicit in => ev(in).head
  }

  case class ListArgumentCombinator[
    ain[a <: List] <: ListArguments[a] { type Args = args1 },
    bin[a <: List] <: ListArguments[a] { type Args = args2 },
    in <: List,
    //
    args1 <: List,
    args2 <: List
  ](
  )(
    implicit
    val ain: ain[in],
    val bin: bin[in]
  ) extends ListArguments[in]
    with ArgumentCombinator[ain[in], bin[in], in]
  {
    type a = ain.Args
    val a: Read[In,a] = ???
    type b = bin.Args
    val b: Read[In, b] = ???
  }

  trait ListArgumentHandling extends Any
  {
    this: Interpretation[_] =>
    final type NoArguments = NoListArguments
  }

}
