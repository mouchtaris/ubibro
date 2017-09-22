package fun
package list
package predicate

import
  typelevel.predicate._,
  Known.{
    itsatype,
    OK,
  }
import
  org.scalatest._

class ListFoldSpec extends FlatSpec with Matchers {

  //
  // Syntax assertions
  //

  "ListFold evidence" should "accept two type parameters" in {
    itsatype[ListFold[Tuple2, Nil]] shouldBe OK
  }

  it should "require the first type argument to be a type-constructor-2" in {
    "itsatype[ListFold[Tuple1, Nil]]" shouldNot typeCheck
  }

  it should "require the second type argument to be a List" in {
    // TODO report scalatest bug
//    "itsatype[ListFold[Tuple2, Int]]" shouldNot typeCheck
  }

  it should "provide an auxiliary type" in {
    itsatype[ListFold.Aux[Tuple2, Nil, Nil]] shouldBe OK
  }

  it should "provide a constructor" in {
    ListFold[Tuple2, Nil, Nil]() shouldBe a[ListFold.any]
  }

  "ListFold() constructor" should "respect the output type" in {
    type out = String
    val lf = ListFold[Tuple2, Nil, out]()
    Known[lf.Out =:= out] should not be null
  }

  "ListFold evidence for Nil" should "not exist" in {
    "Known[ListFold[Tuple2, Nil]]" shouldNot compile
  }

  "ListFold evidence for a single item list" should "not exist" in {
    "Known[ListFold[Tuple2, Int :: Nil]]" shouldNot compile
  }

  "ListFold evidence for lists with more than one item" should "be implicitly available" in {
    Known[ListFold[Tuple2, Int :: Short :: Nil]] shouldBe a[ListFold.any]
  }

  it should "be the right associative result of the type constructor" in {
    type t1 = Int
    type t2 = Double
    type t3 = String
    type t4 = Nothing
    type t5 = Unit
    type t6 = Any
    type t7 = AnyRef
    type t8 = AnyVal
    type list = t1 :: t2 :: t3 :: t4 :: t5 :: t6 :: t7 :: t8 :: Nil

    val lf = Known[ListFold[Tuple2, list]]

    Known[lf.Out =:= (t1, (t2, (t3, (t4, (t5, (t6, (t7, t8)))))))] should not be null
  }
}
