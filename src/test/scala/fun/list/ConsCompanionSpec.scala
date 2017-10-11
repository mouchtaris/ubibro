package fun
package list

import
  org.scalatest._

class ConsCompanionSpec extends FlatSpec with Matchers {

  object El1
  object El2
  type El1 = El1.type
  type El2 = El2.type

  object t extends (El2 :: Nil) {
    val head: El2 = El2
    val tail: Nil = Nil
  }

  "ConsCompanion (::)" should "create lists" in {
    val list = ::(El1, t)

    list.head shouldBe El1
    list.tail shouldBe t
  }

  it should "deconstruct lists" in {
    val list = El1 :: t

    val head :: tail = list

    head shouldBe El1
    tail shouldBe t
  }

}
