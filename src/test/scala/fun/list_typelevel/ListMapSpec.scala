package fun
package list_typelevel

import list._
import typelevel.predicate.known._

import org.scalatest._

class ListMapSpec extends FlatSpec with Matchers {

  import Known.{ itsatype, OK }

  //
  // Syntax assertions
  //

  "A ListMap evidence" should "accept two type parameters" in {
    itsatype[ListMap[Vector, Int :: Double :: Nil]] shouldBe OK
  }

  it should "require a type-constructor-1 as the first type argument" in {
    "itsatype[ListMap[Int, Nil] { type Out = Nil }]" shouldNot typeCheck
  }

  it should "require a list as the second type argument" in {
    // TODO report scalatest bug
//    "itsatype[ListMap[Vector, Int]]" shouldNot typeCheck
  }

  it should "require a list as the resulting output type" in {
    // TODO report scalatest bug
//    "itsatype[ListMap[Vector, Nil] { type Out = Int }]" shouldNot typeCheck
  }

  it should "provide an auxiliary type, refining output types" in {
    type f[a] = Vector[a]
    type l = Nil
    type out = Nil
    new ListMap[f, l] { type Out = out }: ListMap.Aux[f, l, out]
  }

  it should "provide an instance constructor" in {
    ListMap[Vector, Nil, Nil]()
  }

  "Evidence created from the constructor" should "respect the output type" in {
    val lm = ListMap[Vector,  Nil, Int :: Nil]()
    val intAndNil: lm.Out = 12 :: Nil
    intAndNil shouldBe intAndNil
  }

  "Evidence for Nil" should "be implicitly provided" in {
    Known[ListMap[Vector, Nil]] should not be null
  }

  it should "have result type of Nil" in {
    val nilEv = Known[ListMap[Vector, Nil]]
    val nilList: nilEv.Out = Nil
    nilList shouldBe Nil
  }

  "Evidence for any list" should "be implicitly provided" in {
    Known[ListMap[Vector, Int :: String :: Nil]] should not be null
  }

  it should "map all types in the list with type constructor f[_]" in {
    val listEv = Known[ListMap[Vector, Int :: Double :: Nil]]
    val list: listEv.Out = Vector.empty[Int] :: Vector.empty[Double] :: Nil
    list shouldBe list
  }

}
