package fun.typelevel.predicate

import or._

import org.scalatest._

class OrSpec extends FlatSpec with Matchers {

  "Or[] evidence" should "accept two type parameters" in {
    List.empty[Or[Int, Double]] shouldBe empty
  }

  it should "not be implicitly entailed when both a and b are not known" in {
    trait A
    object A extends A
    A.asInstanceOf[Unit]

    trait B
    object B extends B
    B.asInstanceOf[Unit]

    "Known[A `Or` B]" shouldNot compile
  }

  it should "be implicitly entailed when a is known" in {
    trait A
    implicit object A extends A
    object AnotherA extends A
    A.asInstanceOf[Unit]

    trait B
    implicit object B extends B
    B.asInstanceOf[Unit]

    Known[A `Or` B].evidence.left.getOrElse(AnotherA) shouldBe A
  }

  it should "be implicitly entailed when b is known" in {
    trait A
    object A extends A
    A.asInstanceOf[Unit]

    trait B
    implicit object B extends B
    object AnotherB extends B
    B.asInstanceOf[Unit]

    Known[A `Or` B].evidence.right.getOrElse(AnotherB) shouldBe B
  }

  it should "provide a convenience evidence fetcher" in {
    implicit object A
    implicit object B
    Or[A.type, B.type] should not be null
  }

  it should "prioritize evidence A over evidence B" in {
    implicit object A
    implicit object B
    assert(Or[A.type, B.type].evidence.isLeft)
  }

}
