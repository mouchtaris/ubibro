package fun
package list
package typelevel

import
  fun.typelevel._

import
  org.scalatest._

class EquivalentSpec extends FlatSpec with Matchers {

  "Equivalent evidence" should "require two type parameters" in {
    itsatype[Equivalent[Nil, Nil]] shouldBe OK
  }

  it should "require the first type argument to be a List" in {
    "Known[Equivalent[Int, Nil]]" shouldNot typeCheck
  }

  it should "require the second type argume tot be a List" in {
    "Known[Equivalent[Nil, Int]]" shouldNot typeCheck
  }

  it should "provide a Evidence type alias" in {
    type a = Int :: String :: Nil
    type b = String :: Int :: Nil
    Known[Equivalent[a, b]#Evidence =:= And[Subset[a, b], Subset[b, a]]] should not be null
  }

  "Equivalent companion object" should "provide a list type alias" in {
    type a = Int :: String :: Nil
    type b = String :: Int :: Nil
    Known[Equivalent.to[a]#t[b] =:= Equivalent[a, b]] should not be null
  }

  it should "provide a constructor" in {
    type a = Int :: String :: Nil
    type b = String :: Int :: Nil
    Equivalent[a, b](implicitly) shouldBe a[Equivalent.any]
  }

  it should "be implicitly available only for equivalent lists" in {
    type list1 = Nil
    type list2 = Int :: Nil
    type list3a = Int :: String :: Nil
    type list3b = String :: Int :: Nil
    type list4a = Int :: String :: Unit :: Nil
    type list4b = Int :: Unit :: String :: Nil
    type list4c = String :: Int :: Unit :: Nil
    type list4d = String :: Unit :: Int :: Nil
    Known[Equivalent[list1, list1]] should not be null
    Known[Equivalent[list2, list2]] should not be null
    Known[Equivalent[list3a, list3b]] should not be null
    Known[Equivalent[list4a, list4b]] should not be null
    Known[Equivalent[list4b, list4c]] should not be null
    Known[Equivalent[list4c, list4d]] should not be null
    "Known[Equivalent[list4d, java.net.URI :: Nil]]" shouldNot compile
  }

}
