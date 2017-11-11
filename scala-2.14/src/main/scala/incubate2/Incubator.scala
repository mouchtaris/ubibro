package incubate2

import
  Known._,
  Conj._,
  list._,
  arguments._,
  ListArgumentHandling._,
  types._,
  Console.{
    println ⇒ cprintln
  },
  cross.reflect.Api._

final case class ind(n: Int, sigil: String = "  ") {
  val indent: String = Range inclusive (1, n + 1) map (_ ⇒ sigil) mkString
  def println(o: Any): Unit = { Console.println(indent + o.toString) }
  def next = copy(n = n + 1)
  def withSigil(sig: String) = copy(sigil = sig)
}

object TypeInfo {

  case object Alias {
    def unapply(tt: Type): Boolean =
      tt.dealias != tt
  }

  case object Baseable {
    def unapply(tt: Type): Option[Seq[Type]] =
      tt match {
        case st @ SingleType(pre, sym) ⇒
          Some {
            st.baseClasses
              .map { st.baseType }
          }
        case _ ⇒
          None
      }
  }

  def typeinfo(tt: Type)(
    implicit
    sb: StringBuilder = new StringBuilder,
    ind: ind = incubate2.ind(0),
    mark: String = " |-|"
  ): String = {
    import ind._
    sb ++= indent ++= mark ++= tt.toString ++= "\n"
    //sb ++= tt.getClass.toString ++= "\n"
    tt match {
      case Alias() ⇒
        typeinfo(tt.dealias)(sb, ind, " ~> ")
      case Baseable(bases) ⇒
        bases.foreach { base ⇒
          typeinfo(base)(sb, ind, " => ")
        }
        sb.toString
      case _ ⇒
        tt.typeArgs.foreach { typeinfo(_)(sb, ind.next) }
        sb.toString
    }

  }

  def typeinfo[T: TypeTag]: String =
    typeinfo(typeOf[T])
}
import TypeInfo._

object pkg extends AnyRef
{

  final implicit class ListCons[self <: List](val self: self) extends AnyVal {
    def ::[h](h: h): h :: self = Cons(h, self)
  }

  type ***[a <: Arguments, b <: Arguments] =
    a `CombinedArguments` b
  type **[a[_], b[_]] =
    Argument[a] *** Argument[b]

  trait Eval[args <: Arguments] {
    type Args = args
    type Handler[in] <: ArgumentsHandler[args, in]
    type Out
    def apply[in: Handler](in: in): Out
    def handler[in](implicit han: Handler[in]): han.type = han
  }

  trait ArgEval[T[_]]
    extends Eval[Argument[T]]
  {
    final type Handler[in] = ArgumentHandler[T, in]
  }

  trait CombinedEval[
    args1 <: Arguments,
    args2 <: Arguments
  ] extends Eval[args1 *** args2]
  {
    final type Args1 = args1
    final type Args2 = args2
    type Handler[in] <: CombinedArgumentsHandler[Args1, Args2, in]
  }

  final type int_t[a] = IsType[Int]#t[a]

  final case object IntEval
    extends AnyRef
    with ArgEval[int_t]
  {
    type Out = Int
    def apply[in](in: in)(implicit han: Handler[in]): Out =
      han.evidence(han.in2a(in))
  }

  final case object PlusEval
    extends AnyRef
    with CombinedEval[IntEval.Args, IntEval.Args]
  {
    type Out = Int
    type Handler[in] = CombinedArgumentsHandler[Args1, Args2, in]
    def apply[in](in: in)(implicit han: Handler[in]): Out = {
      12
    }
  }

  type In = Int :: Int :: Nil.type
  def test = {
    import PlusEval._
    cprintln { typeinfo[Handler[In]] }
    cprintln { apply(12 :: 12 :: Nil) }
  }
}


object Incubator {

  def entry(args: Array[String]): Unit = {
    Console.println("Incubating 2 ....")
    pkg.test
//    Tests().foreach(_())
  }

}

object Tests {
  def ind0: ind = ind(0)
  import pkg._
  final case class test(name: Any, ind: ind = ind0)(block: ind ⇒ Unit) {
    import ind._
    def apply(): Unit = { println("*** Tessting " + name.toString); block(ind.next) }
  }
  def test_evalplus = test("eval plus") { implicit ind ⇒
    import ind._
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
      test_evalplus
    )

}
