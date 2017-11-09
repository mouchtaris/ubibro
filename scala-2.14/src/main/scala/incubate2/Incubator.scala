package incubate2 

import
  Known._,
  Conj._,
  list._

object pkg extends AnyRef
{
  trait ListConcat[a <: List, b <: List] {
    type Out <: List
  }
  case object ListConcat {
    final abstract class resultOf[a <: List, b <: List] {
      type t[out <: List] = ListConcat[a, b] {
        type Out = out
      }
    }

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


  final implicit class FreverseCompose[A, B](val self: (A ⇒ B)) extends AnyVal {
    def <<[C](f: C ⇒ A): C ⇒ B = f andThen self
  }

  object ListHandling {

    trait ListArgumentsHandler[rest <: List] extends AnyRef
    {
      this: pkg.ArgumentsHandler[_, _] ⇒

      final type Remainder = rest
      val consume: In ⇒ Remainder
    }

    case class ArgumentsHandler[args <: Arguments, rest <: List, in <: List](
      consume: in ⇒ rest
    ) extends AnyRef
      with ListArgumentsHandler[rest]
      with pkg.ArgumentsHandler[args, in]

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
      with pkg.ArgumentHandler[T, in]
    {
      type A = a
      val evidence: T[A] = implicitly
    }

    case class CombinedArgumentsHandler[
      args1 <: Arguments,
      han1 <: pkg.ArgumentsHandler[args1, in],
      args2 <: Arguments,
      han2 <: pkg.ArgumentsHandler[args2, in],
      rest <: List,
      in <: List
    ](
      handler1: han1,
      handler2: han2,
      consume: in ⇒ rest
    ) extends AnyRef
      with ListArgumentsHandler[rest]
      with pkg.CombinedArgumentsHandler[args1, han1, args2, han2, in]

    implicit def listArgumentHandler[T[_], h: T, t <: List]: ArgumentHandler[T, h, t, h :: t] =
      ArgumentHandler(_.head, _.tail)

    implicit def listCombinedArgumentHandler[
      args1 <: Arguments,
      args2 <: Arguments,
      rest1 <: List,
      rest2 <: List,
      in <: List
    ](
      implicit dummyImplicit: DummyImplicit,
      han1: pkg.ArgumentsHandler[args1, in] with ListArgumentsHandler[rest1],
      han2: pkg.ArgumentsHandler[args2, rest1] with ListArgumentsHandler[rest2]
    ): CombinedArgumentsHandler[args1, han1.type, args2, ArgumentsHandler[args2, rest2, in], rest2, in] = {
      val superconsumer: in ⇒ rest2 = han1.consume andThen han2.consume
      CombinedArgumentsHandler(
        han1,
        ArgumentsHandler(superconsumer),
        superconsumer
      )
    }

  }

  final case class Clue[a]()
  final case class Plue[a]()
  final case object Ibo {
    implicit val clue: Clue[Ibo] = Clue()
    implicit val plue: Plue[Ibo] = Plue()
  }
  final type Ibo = Ibo.type
  import ListHandling._
  trait AH[in] {
    type A <: Arguments
    final type H = ArgumentsHandler[A, in]
    def handler(implicit h: H): h.type = h
  }
  trait AF1[T[_], in] extends AH[in] { final type A = Argument[T] }
  trait AF2[A1 <: Arguments, A2 <: Arguments, in] extends AH[in] { final type A = CombinedArguments[A1, A2] }
  type Tail = Int :: String :: Nil
  type In = Ibo :: Ibo :: Tail
  val In = Cons(Ibo, Cons(Ibo, Cons(12, Cons("Ibo", Nil)))); implicitly[In <:< In]
  object A1 extends AF1[Clue, In]
  object A2 extends AF1[Plue, In]
  // DOTTY INVESTIGATION
  //implicitly[handling.ArgumentHandler[Clue, Ibo, Ibo :: Tail]]//(listArgumentHandler(Ibo.clue))
  //implicitly[ArgumentsHandler[Argument[Clue], In]]
  //implicitly[A1.H]
  val han1 = A1.handler
  object A12 extends AF2[A1.A, A2.A, In]
  lazy val han12 = A12.handler
}


object Incubator {

  def main(args: Array[String]): Unit = {
    Console.println("Incubating 2 ....")
    import scala.reflect.runtime.universe._
    Console.println { typeOf[pkg.han12.handler1.Remainder].dealias }
//    println { han1 in2a In }
//    Tests().foreach(_())
  }
}

trait Ind {
  final case class ind(n: Int) {
    val indent: String = Range inclusive (1, n + 1) map (_ ⇒ "  ") mkString
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
  }
  def test_listargscombenc1 = test("listargcombenc1") { implicit ind =>
  }
  def test_&& = test(&&) { implicit ind ⇒
    import ind._
    trait A; trait B
    implicit case object A extends A
    implicit case object B extends B
    println { known[A && B] }
  }

  def test_list = test("::") { implicit ind ⇒
    import ind._
    println { Cons(12, Cons("Hello", Nil)) }
  }

  def apply(): Vector[test] =
    Vector(
      test_&&,
      test_list,
      test_concat,
      test_listargscombenc1
    )

}
