package fun
package list
package typelevel

import
  fun.typelevel._,
  fun.typelevel.predicate._
import
  org.scalatest._

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
    itsatype[ForAll[list, clue]] shouldBe OK
  }

  it should "require the first type argument to be a list" in {
    // TODO report scalatest bug
//    "itsatype[ForAll[Int, clue]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a predicate" in {
    "itsatype[ForAll[list, Tuple2]]" shouldNot typeCheck
  }

  "ForAll companion object" should "provide a constructor" in {
    ForAll[list, Tuple1]() shouldBe a[ForAll.any]
  }

  it should "provide an \"any\" type" in {
    itsatype[ForAll.any] shouldBe OK
  }

  "ForAll.pred[p]#t[list]" should "be an alias for ForAll" in {
    type pred[a] = Tuple1[a]
    type list = Int :: String :: Nil
    Known[ForAll.pred[pred]#t[list] =:= ForAll[list, pred]] should not be null
  }

  "Evidence for ForAll" should "be implicitly available if implicit evidence exist for all types" in {
    Known[ForAll[list, clue]] shouldBe a[ForAll.any]
  }

  it should "not be available if there is no implicit evidence for one of the types" in {
    "Known[ForAll[NoClue :: list, clue]]" shouldNot compile
  }

}
