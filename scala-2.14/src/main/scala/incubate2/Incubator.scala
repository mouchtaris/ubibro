package incubate2 

import
  Known._,
  Conj._,
  list._,
  arguments._,
  ListArgumentsHandling._

object pkg extends AnyRef
{

}


object Incubator {

  def main(args: Array[String]): Unit = {
    Console.println("Incubating 2 ....")
    Tests().foreach(_())
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
      test_listargscombenc1
    )

}
