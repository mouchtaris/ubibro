package fun
package list_predicate

import list._
import typelevel.predicate.known._

import org.scalatest._

class ForAllSpec extends FlatSpec with Matchers {

  trait clue[_]
  implicit object clue1 extends clue[Int]
  implicit object clue2 extends clue[String]
  implicit object clue3 extends clue[Short]
  implicit object clue4 extends clue[Double]
  implicit object clue5 extends clue[Unit]

  type list = Int :: String :: Short :: Double :: Unit :: Nil

  type NoClue = java.net.URI

  "ForAll evidence" should "have two type parameters" in {
    Known[ForAll[list, clue]] should not be null
  }

  it should "require the first type argument to be a list" in {
    "Known[ForAll[Int, clue]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a predicate" in {
    "Known[ForAll[list, Tuple2]]" shouldNot typeCheck
  }

  "ForAll companion object" should "provide a constructor" in {
    ForAll[list, Tuple1]() should not be null
  }

  "Evidence for ForAll" should "be implicitly available if implicit evidence exist for all types" in {
    Known[ForAll[list, clue]] should not be null
  }

  it should "not be available if there is no implicit evidence for one of the types" in {
    "Known[ForAll[NoClue :: list, clue]]" shouldNot compile
  }

}
