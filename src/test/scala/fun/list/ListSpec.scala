package fun
package list

import
  typelevel.predicate._,
  Known.{
    itsatype,
    OK,
  }
import
  org.scalatest._

class ListSpec extends FlatSpec with Matchers {

  trait U
  trait A extends U
  trait B extends U
  trait AA extends A
  trait BB extends B

  object A extends A
  object B extends B
  object AA extends AA
  object BB extends BB

  type ListA = A :: B :: Nil
  type ListSubHead = AA :: B :: Nil
  type ListSubTail = A :: BB :: Nil

  //
  // Type-assertive tests
  //
  "A list" should "have two parameter types" in {
    itsatype[Cons[Int, Nil]] shouldBe OK
  }

  "A list" should "be type aliased as ::" in {
    Known[(Int :: Nil) =:= Cons[Int, Nil]] should not be null
  }

  "A list" should "provide a terminal Nil list" in {
    Nil shouldBe Nil
  }

  "A list" should "be covariant on it's Head type" in {
    Known[ListSubHead <:< ListA] should not be null
  }

  "A list" should "be covariant on it's Tail type" in {
    Known[ListSubTail <:< ListA] should not be null
  }

  "Package list" should "provide ::() to prepend" in {
    (12 :: Nil) should not be null
  }

  "Package list" should "provide a List type alias" in {
    Known[(Int :: Nil) <:< List] should not be null
  }

  "A list's tail" should "be a stable name" in {
    val list: List = Nil
    import list.tail.tail.tail
    tail shouldBe tail
  }

  //
  // Runtime behaviour tests
  //

  "A list" should "return the head it's constructed with" in {
    val head = new Object
    (head :: Nil).head shouldBe head
  }

  "A list" should "return the tail it's constructed with" in {
    val head = new Object
    val tail = new Object :: Nil
    (head :: tail).tail shouldBe tail
  }

  "Accessing Nil's head" should "be a nil.error.HeadAccess error" in {
    assertThrows[nil.error.HeadAccess] {
      Nil.head
    }
  }

  "Accessing Nil's tail" should "return Nil itself" in {
    Nil.tail shouldBe Nil
  }

}
