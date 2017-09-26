package fun
package list
package typelevel

import
  fun.typelevel._
import
  org.scalatest._

class ContainsSpec extends FlatSpec with Matchers {

  "Contains evidence" should "accept two type parameters" in {
    itsatype[Contains[Nil, Nothing]] shouldBe OK
  }

  it should "require the first type to be a list" in {
    "Known[Contains[Int, Int]]" shouldNot typeCheck
  }

  "Contains companion object" should "provide an instance constructor" in {
    Contains[Nil, Int]() shouldBe a[Contains.any]
  }

  it should "provide a convenience \"any\" type" in {
    Known[Contains[Nil, Nil] <:< Contains.any] should not be null
  }

  "Contains.typ[a]#t[list]" should "be Contains[list, a]" in {
    type t1 = Int
    type list = Nil
    Known[Contains.typ[t1]#t[list] =:= Contains[list, t1]] should not be null
  }

  "Contains.in[list]#t[a]" should "be Contains[list, a]" in {
    type t1 = Int
    type list = Nil
    Known[Contains.in[list]#t[t1] =:= Contains[list, t1]] should not be null
  }

  "An empty list" should "contain nothing" in {
    "Known[Contains[Nil, Any]]" shouldNot compile
  }

  "A list of one item" should "contain this item" in {
    type t1 = Int
    type list = t1 :: Nil
    Known[Contains[list, t1]] should not be null
  }

  "A list of more than one items" should "contain a type that is contained in the head" in {
    type t1 = Int
    type list = t1 :: String :: Nil
    Known[Contains[list, t1]] should not be null
  }

  it should "contain a type that is contained in the tail" in {
    type t1 = Int
    type list = String :: t1 :: Nil
    Known[Contains[list, t1]] should not be null
  }

  it should "not contain a type that is not contained in the list" in {
    type t1 = Int
    type list = Double :: String :: Nil
    "Known[Contains[list, t1]]" shouldNot compile
  }
}
