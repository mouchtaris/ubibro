package incubate2 

import Known._
import Conj._
import list._

object pigcontext extends
  pigs.PigContext
  with list_pigs.ListPigs

import pigcontext._

object pkg extends AnyRef
{
  trait ListConcat[a <: List, b <: List] {
    type Out <: List
  }
  case object ListConcat {
    final case class create[a <: List, b <: List, out <: List]()
      extends ListConcat[a, b]
    {
      final type Out = out
    }

    implicit def nilcat[b <: List]: create[Nil, b, b] = create()

    implicit def htcat[h, t <: List, b <: List](
      implicit
      tbcat: ListConcat[t, b]
    ): create[h :: t, b, h :: tbcat.Out] = create()

    // weirdoes
    implicit def hcat[h, b <: List]: create[h :: List, b, h :: b] = create()
  }


  trait Arguments extends Any
  trait Argument[T] extends Any with Arguments
  trait NoArgument extends Any with Arguments
  trait CombinedArguments[arg1 <: Arguments, arg2 <: Arguments] extends Any with Arguments

  trait ListEncoding[args <: Arguments] { type Encoding <: List }
  object ListEncoding {
    final abstract class encodingOf[args <: Arguments] {
      final type t[enc <: List] = ListEncoding[args] {
        type Encoding = enc
      }
    }

    final case class create[args <: Arguments, enc <: List]()
      extends ListEncoding[args]
    {
      final type Encoding = enc
    }

    implicit def argument[T]: create[Argument[T], T :: List] =
      create()

    implicit def combined[
      arg1 <: Arguments,
      arg2 <: Arguments,
      enc1 <: List: ListEncoding.encodingOf[arg1]#t,
      enc2 <: List: ListEncoding.encodingOf[arg2]#t
    ](
      implicit
      cat: ListConcat[enc1, enc2]
    ): create[CombinedArguments[arg1, arg2], cat.Out] =
      create()
  }

//  trait ArgumentsHandler[in]
//  trait ArgumentHandler[arg <: Argument, in] extends ArgumentsHandler[in] {
//    val arg: arg
//    def apply(in: in): arg.T
//  }
//  trait CombinedArgumentsHandler[arg <: CombinedArguments, in] extends ArgumentsHandler[in] {
//    val arg: arg
//    def a(in: in): arg.Arg1
//  }
//
}


object Incubator {
  def main(args: Array[String]): Unit = {
    println("Incubating 2 ....")
    Tests().foreach(_())
  }
}

trait Ind {
  final case class ind(n: Int) {
    val indent: String = 1 to (n + 1) map (_ ⇒ "  ") mkString
    def println(o: Any): Unit = { Console.println(indent + o.toString) }
    def next = ind(n + 1)
  }
}
trait TestLow extends Ind {
  def ind0: ind = ind(0)
}
object Tests extends TestLow {
  import pkg._
  final case class test(name: Any, ind: ind = ind0)(block: ind ⇒ Unit) {
    import ind._
    def apply(): Unit = { println("*** Tessting " + name.toString); block(ind.next) }
  }
  def test_concat = test(ListConcat) { implicit ind =>
    import ind._
    val cat = known[ ListConcat[ Int :: String :: Nil, Float :: Double :: Nil ] ]
    println(cat)
    println { pig[cat.Out] }
  }
  def test_listargscombenc1 = test("listargcombenc1") { implicit ind =>
    import ind._
    val enc = known[ ListEncoding[ CombinedArguments[ Argument[Int], Argument[String] ] ] ]
    println(enc)
    println { pig[enc.Encoding] }
  }
  def test_pigs = test("PIGS") { implicit ind =>
    import ind._
    println(Seq(
      pig[Int],
      pig[String],
      pig[Nil],
      pig[Int :: Nil],
      pig[Int :: String :: Float :: Double :: Unit :: Nil]
    ))
  }
  def test_&& = test(&&) { implicit ind ⇒
    import ind._
    trait A; trait B
    implicit case object A extends A
    implicit case object B extends B
    println { known[A && B] }
  }

  def test_list = test(::) { implicit ind ⇒
    import ind._
    println { Cons(12, Cons("Hello", Nil)) }
  }

  def apply(): Vector[test] =
    Vector(
      test_&&,
      test_list,
      test_concat,
      test_listargscombenc1,
      test_pigs
    )

}
