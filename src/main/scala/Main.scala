object Main extends core {

  trait ListArguments[in <: List] extends Arguments[in] {
    type Args <: List
  }

  case class NoListArgument[in <: List]()
    extends ListArguments[in]
    with NoArguments[in]
  {
    type Args = Nil
  }

  case class ListArgument[h, in <: List](
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
    val bin: bin[in],

  ) extends ListArguments[in]
    with ArgumentCombinator[ain[in], bin[in], in]
  {
    type a = ain.Args
    val a: Read[In,a] = ???
    type b = bin.Args
    val b: Read[In, b] = ???
  }

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
  }

  def msg = "I was compiled by dotty :)"

}
