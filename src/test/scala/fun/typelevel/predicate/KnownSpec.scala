package fun.typelevel.predicate

import known._
import org.scalatest._


class KnownSpec extends FlatSpec with Matchers {

  sealed trait Shape
  case object Flat extends Shape

  trait Fact { type Earth }
  implicit object Fact extends Fact {
    type Earth = Flat.type
  }

  //
  // Type-assertive tests (syntax)
  //

  "Known" should "accept a type parameter" in {
    val known: Known[Int] = 12
    known shouldBe known
  }

  "Known" should "retain type refinement information on the implicit value used" in {
    val fact = Known[Fact]
    val shape: fact.Earth = Flat
    shape shouldBe Flat
  }

  "Known" should "work for singleton types as well" in {
    implicit object Drogon
    Known[Drogon.type] shouldBe Drogon
  }

  //
  // Runtime behaviour
  //

  "Known" should "return what is known" in {
    Known[Fact] shouldBe Fact
  }

}
