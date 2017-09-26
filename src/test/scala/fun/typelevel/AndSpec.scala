package fun.typelevel

import
  org.scalatest._

class AndSpec extends FlatSpec with Matchers {

  //
  // Syntax specs
  //

  "And[] evidence" should "accept two type parameters" in {
    List.empty[And[Int, Double]] shouldBe empty
  }

  it should "be implicitly entailed when both a and b are known" in {
    trait A
    implicit object A extends A

    trait B
    implicit object B extends B

    Known[A `And` B] should not be null
  }

  it should "not be implicitly entailed when a is not known" in {
    trait A
    object A extends A
    A.asInstanceOf[Unit]

    trait B
    implicit object B extends B
    B.asInstanceOf[Unit]

    "Known[A `And` B]" shouldNot compile
  }

  it should "not be implicitly entailed when b is not known" in {
    trait A
    implicit object A extends A
    A.asInstanceOf[Unit]

    trait B
    object B extends B
    B.asInstanceOf[Unit]

    "Known[A `And` B]" shouldNot compile
  }

  it should "provide a convenience evidence fetcher" in {
    implicit object A
    implicit object B
    And[A.type, B.type] should not be null
  }

}
