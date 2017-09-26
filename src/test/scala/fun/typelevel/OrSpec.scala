package fun
package typelevel

import
  org.scalatest._

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
    A.asInstanceOf[Unit]

    trait B
    implicit object B extends B
    B.asInstanceOf[Unit]

    val or = Known[A `Or` B]

    Known[or.Out =:= A] should not be null
    or.evidence shouldBe A
  }

  it should "be implicitly entailed when b is known" in {
    trait A
    object A extends A
    A.asInstanceOf[Unit]

    trait B
    implicit object B extends B
    B.asInstanceOf[Unit]

    val or = Known[A `Or` B]

    Known[or.Out =:= B] should not be null
    or.evidence shouldBe B
  }

  "Or companion object" should "provide a convenience evidence fetcher" in {
    implicit object A
    implicit object B
    Or[A.type, B.type] should not be null
  }

  it should "prioritize evidence A over evidence B" in {
    implicit object A
    implicit object B

    val or = Or[A.type, B.type]

    Known[or.Out =:= A.type] should not be null
  }

  it should "provide a type alias for Out" in {
    itsatype[Or.Aux[Int, Double, String]] shouldBe OK
  }

  "Or.Aux" should "refine the output type" in {
    type out = String
    val or = Or[Int, Double, out]("")

    Known[or.Out =:= out] should not be null
  }

  "Or.resultOf" should "be an alias for Aux" in {
    type a = Int
    type b = String
    type out = java.net.URI
    Known[Or.resultOf[a, b]#t[out] =:= Or.Aux[a, b, out]] should not be null
  }
}
