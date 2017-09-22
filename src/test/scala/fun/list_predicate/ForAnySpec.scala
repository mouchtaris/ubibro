package fun
package list_predicate

import list._
import typelevel.predicate.known._

import org.scalatest._

class ForAnySpec extends FlatSpec with Matchers {

  trait clue[_]
  implicit object clue1 extends clue[Int]

  type list = Int :: String :: Short :: Double :: Unit :: Nil

  type NoClue = java.net.URI

  "ForAny evidence" should "have two type parameters" in {
    Known[ForAny[list, clue]] should not be null
  }

  it should "require the first type argument to be a list" in {
    "Known[ForAny[Int, clue]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a predicate" in {
    "Known[ForAny[list, Tuple2]]" shouldNot typeCheck
  }

  "ForAny companion object" should "provide a constructor" in {
    ForAny[list, Tuple1]() should not be null
  }

  "Evidence for ForAny" should "be implicitly available if implicit evidence exists for any type" in {
    Known[ForAny[list, clue]] should not be null
  }

  it should "not be available if there is no implicit evidence for any type in the list" in {
    "Known[ForAny[NoClue :: Nil, clue]]" shouldNot compile
  }
}
