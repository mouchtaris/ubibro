package fun.list

import org.scalatest._

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
    val list: Cons[Int, Nil] = 12 :: Nil
    list shouldBe list
  }

  "A list" should "be type aliased as ::" in {
    val list: Int :: Nil = 12 :: Nil
    list shouldBe list
  }

  "A list" should "provide a terminal Nil list" in {
    Nil shouldBe Nil
  }

  "A list" should "be covariant on it's Head type" in {
    val sub: ListSubHead = AA :: B :: Nil
    val list: ListA = sub
    list shouldBe sub
  }

  "A list" should "be covariant on it's Tail type" in {
    val sub: ListSubTail = A :: BB :: Nil
    val list: ListA = sub
    list shouldBe sub
  }

  "Package list" should "provide ::() to prepend" in {
    val list = 12 :: Nil
    list shouldBe list
  }

  "Package list" should "provide a List type alias" in {
    def acceptList(l: List): l.type = l
    acceptList(12 :: Nil) should not be null
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
