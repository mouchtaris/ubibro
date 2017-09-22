package fun.typelevel

import
  org.scalatest._

class LubSpec extends FlatSpec with Matchers {

  "A LUB type" should "accept two types" in {
    type a = String
    type b = Vector[Nothing]

    new Lub[a, b] { } should not be null
  }

  "A LUB type's a" should "be type A" in {
    type T = Int
    val value: T = 12

    val va: Lub[T, Nothing]#A = value
    value shouldBe va
  }

  "A LUB type's b" should "be type B" in {
    type T = String
    val value: T = "string"

    val vb: Lub[Nothing, T]#B = value
    value shouldBe vb
  }

  trait u
  trait a extends u
  trait b extends u

  "A LUB type's lub" should "be type Out" in {
    val lub = Lub[a, b, u]()
    val value: lub.Out = new u{ }

    value shouldBe value
  }

  "A LUB resolution" should "return a type that can hold both types" in {
    val lub = Lub[a, b]
    import lub.{ Out => LUB }

    val a = new a { }
    val b = new b { }
    val jesus: (LUB, LUB) = (a, b)
    jesus shouldBe jesus
  }

  "A LUB instance" should "be case-class constructable" in {
    val lub = Lub[a, b, u]()
    lub shouldBe lub
  }

  "A LUB instance" should "be implicitly available" in {
    val lub = implicitly[Lub[a, b]]
    lub shouldBe lub
  }
}
