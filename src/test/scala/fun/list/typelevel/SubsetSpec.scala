package fun
package list
package typelevel

import
  fun.typelevel._
import
  org.scalatest._

class SubsetSpec extends FlatSpec with Matchers {

  "Subset" should "accept two type parameters" in {
    itsatype[Subset[Nil, Nil]] shouldBe OK
  }

  it should "require the first type argument to be a list" in {
    // TODO report scalatest bug
//    "itsatype[Subset[Int, Nil]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a list" in {
    // TODO report scalatest bug
//    "itsatype[Subset[Nil, Int]]" shouldNot typeCheck
  }

  "Subset companion object" should "provide a convenience \"any\" type" in {
    Known[Subset[Nil, Nil] <:< Subset.any] should not be null
  }

  it should "provide an instance constructor" in {
    Subset[Int :: Nil, String :: Nil]() shouldBe a[Subset.any]
  }

  "Subset implicit evidence" should "be known for Nil as the subset of any list type" in {
    Known[Subset[Nil, Nil]] should not be null
    Known[Subset[Nil, Int :: Nil]] should not be null
    Known[Subset[Nil, Int :: String :: Nil]] should not be null
  }

  it should "be known for subset lists" in {
    type superlist = Int :: String :: Unit :: Nil
    type sublist1 = Int :: String :: Nil
    type sublist2 = Int :: Unit :: Nil
    type sublist3 = String :: Unit :: Nil
    type sublist4 = Int :: Nil
    type sublist5 = String :: Nil
    type sublist6 = Unit :: Nil
    Known[Subset[sublist1, superlist]] should not be null
    Known[Subset[sublist2, superlist]] should not be null
    Known[Subset[sublist3, superlist]] should not be null
    Known[Subset[sublist4, superlist]] should not be null
    Known[Subset[sublist5, superlist]] should not be null
    Known[Subset[sublist6, superlist]] should not be null
  }

  it should "be agnostic on the order of types in the list" in {
    type superlist = Int :: String :: Unit :: Nil
    type sublist1 = String :: Unit :: Int :: Nil
    type sublist2 = Unit :: String :: Int :: Nil
    type sublist3 = Unit :: String :: Nil
    type sublist4 = Unit :: Int :: Nil
    Known[Subset[sublist1, superlist]] should not be null
    Known[Subset[sublist2, superlist]] should not be null
    Known[Subset[sublist3, superlist]] should not be null
    Known[Subset[sublist4, superlist]] should not be null
  }

}
