package incubate2 

import scala.annotation.tailrec

object pkg {
  type Known[T] = T
  def known[t <: AnyRef](implicit t: t): t.type = t
  trait &&[A, B] {
    val a: A
    val b: B
  }
  case object && {
    final case class &&[A, B]()(
      implicit
      val a: A,
      val b: B
    ) extends pkg.&&[A, B]
    implicit def `⇒ a && b`[a: Known, b: Known]: a && b = &&()
  }

  type List = _ :: _
  sealed trait ::[h, t <: List] extends Any {
    def head: h
    def tail: t
    final override def toString: String = appendTo(new StringBuilder).toString
    def appendTo(sb: StringBuilder): sb.type
  }
  case object :: {
    final case class ::[h, t <: List](head: h, tail: t) extends pkg.::[h, t] {
      def appendTo(sb: StringBuilder): sb.type = {
        sb append head append " :: "
        tail appendTo sb
      }
    }
    def apply[h, t <: List](h: h, t: t): h :: t = ::(h, t)
  }
  sealed trait Nil extends (Nil :: Nil)
  final case object Nil extends Nil {
    val head = this
    def tail = this
    def appendTo(sb: StringBuilder): sb.type = {
      sb append "Nil"
      sb
    }
  }

  trait TypeAliasT[t] extends Any { final type T = t }
  trait TypeAliasArgs1[args1] extends Any { final type Args1 = args1 }
  trait TypeAliasArgs2[args2] extends Any { final type Args2 = args2 }

  trait ArgumentEncoding {
    trait Arguments
    type Argument0 <: Arguments
    type Argument1[T] <: Arguments with TypeAliasT[T] 
    type ArgumentsCombination[args1 <: Arguments, args2 <: Arguments] <: Arguments with TypeAliasArgs1[args1] with TypeAliasArgs2[args2]
  }

  trait ListArguments { type Encoding <: List }
  trait ListArgument0 extends ListArguments { final type Encoding = Nil }
  trait ListArgument1[T] extends ListArguments with TypeAliasT[T] { final type Encoding = T :: List }
  trait ListArgumentsCombination[args1 <: ListArguments, args2 <: ListArguments] extends ListArguments with TypeAliasArgs1[args1] with TypeAliasArgs2[args2] {
    type Encoding <: List
    val args1: args1
  }
  object ListArgumentsCombination {
    def apply(args1_ : ListArguments, args2_ : ListArguments) = new ListArgumentsCombination[args1_.type, args2_.type] { val args1 = args1_ }
  }

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
  def test_listargscombenc1 = test("listargcombenc1") { implicit ind =>
    import ind._
    val args1 = new ListArgument1[Int] { }
    val args2 = new ListArgument1[String] { }
    val args = ListArgumentsCombination(args1, args2)
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
    println { ::(12, ::("Hello", Nil)) }
  }

  def apply(): Vector[test] =
    Vector(
      test_&&,
      test_list,
      test_listargscombenc1
    )

}
