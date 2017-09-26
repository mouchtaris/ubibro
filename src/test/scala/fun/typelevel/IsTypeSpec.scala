package fun
package typelevel

import
  org.scalatest._

class IsTypeSpec extends FlatSpec with Matchers {

  "IsType evidence" should "accept two type params" in {
    itsatype[IsType[Int, Double]] shouldBe OK
  }

  "IsType companion object" should "provide a constructor" in {
    IsType(implicitly[Int =:= Int]) shouldBe a[IsType.any]
  }

  it should "provide an \"any\" type alias" in {
    itsatype[IsType.any] shouldBe OK
  }

  it should "provide a \"is[]\" auxiliary predicate type constructor" in {
    itsatype[IsType.is[Int]#t[Int]] shouldBe OK
  }

  "Implicit evidence" should "be implicitly available for same types" in {
    type t = Int
    Known[IsType[t, t]] should not be null
  }

  it should "not be implicitly available for non-same types" in {
    "Known[IsType[Int, String]]" shouldNot compile
  }

}
