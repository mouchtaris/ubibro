package fun
package list
package typelevel

import
  fun.typelevel._
import
  org.scalatest._

class ListMapSpec extends FlatSpec with Matchers {

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

  "ListMap companion object" should "provide an auxiliary type" in {
    itsatype[ListMap.Aux[Tuple1, Nil, String :: Nil]] shouldBe OK
  }

  it should "provide an instance constructor" in {
    ListMap[Vector, Nil, Nil]() shouldBe a[ListMap.any]
  }

  it should "provide an \"any\" type" in {
    itsatype[ListMap.any] shouldBe OK
  }

  "ListMap.Aux" should "refine the output type" in {
    type out = Nil
    Known[ListMap.Aux[Vector, Nil, out]#Out =:= out] should not be null
  }

  "Evidence created from the constructor" should "respect the output type" in {
    type out = String :: Nil
    val lm = ListMap[Vector,  Nil, out]()
    Known[lm.Out =:= out] should not be null
  }

  "Evidence for Nil" should "be implicitly provided" in {
    Known[ListMap[Vector, Nil]] shouldBe a[ListMap.any]
  }

  it should "have result type of Nil" in {
    val nilEv = Known[ListMap[Vector, Nil]]
    Known[nilEv.Out =:= Nil] should not be null
  }

  "Evidence for any list" should "be implicitly provided" in {
    Known[ListMap[Vector, Int :: String :: Nil]] shouldBe a[ListMap.any]
  }

  it should "map all types in the list with type constructor f[_]" in {
    val listEv = Known[ListMap[Vector, Int :: Double :: Nil]]
    Known[listEv.Out =:= (Vector[Int] :: Vector[Double] :: Nil)] should not be null
  }

}
