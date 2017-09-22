package fun
package list
package predicate

import
  typelevel._,
  predicate._,
  Known.{
    itsatype,
    OK,
  }
import
  org.scalatest._

class ForAnySpec extends FlatSpec with Matchers {

  trait clue[_]
  implicit object clue1 extends clue[Int]

  type list = Int :: String :: Short :: Double :: Unit :: Nil

  type NoClue = java.net.URI

  "ForAny evidence" should "have two type parameters" in {
    itsatype[ForAny[list, clue]] shouldBe OK
  }

  it should "require the first type argument to be a list" in {
    // TODO report scalatest bug
//    "itsatype[ForAny[Int, clue]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a predicate" in {
    "itsatype[ForAny[list, Tuple2]]" shouldNot typeCheck
  }

  "ForAny companion object" should "provide a constructor" in {
    ForAny[list, Tuple1]() shouldBe a[ForAny.any]
  }

  it should "provide an \"any\" type" in {
    itsatype[ForAny.any] shouldBe OK
  }

  "Evidence for ForAny" should "be implicitly available if implicit evidence exists for any type" in {
    Known[ForAny[list, clue]] shouldBe a[ForAny.any]
  }

  it should "not be available if there is no implicit evidence for any type in the list" in {
    "Known[ForAny[NoClue :: Nil, clue]]" shouldNot compile
  }
}
