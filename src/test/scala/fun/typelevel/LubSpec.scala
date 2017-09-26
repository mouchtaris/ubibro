package fun.typelevel

import
  org.scalatest._

class LubSpec extends FlatSpec with Matchers {

  "A LUB type" should "accept two types" in {
    type a = String
    type b = Vector[Nothing]
    itsatype[Lub[a, b]] shouldBe OK
  }

  "A LUB type's a" should "be type A" in {
    type T = Int
    Known[Lub[T, Nothing]#A =:= T] should not be null
  }

  "A LUB type's b" should "be type B" in {
    type T = String
    Known[Lub[Nothing, T]#B =:= T] should not be null
  }

  trait u
  trait a extends u
  trait b extends u

  "A LUB type's lub" should "be type Out" in {
    val lub = Lub[a, b, u]()
    Known[lub.Out =:= u] should not be null
  }

  "A LUB resolution" should "return a type that can hold both types" in {
    val lub = Lub[a, b]
    import lub.{ Out => LUB }
    Known[(a <:< LUB) `And` (b <:< LUB)] should not be null
  }

  "A LUB instance" should "be case-class constructable" in {
    val lub = Lub[a, b, u]()
    lub shouldBe lub
  }

  it should "be implicitly available" in {
    val lub = implicitly[Lub[a, b]]
    lub shouldBe lub
  }

  "Lub.of[a, b]#t[u]" should "be an alias of Lub.Aux[a, b, u]" in {
    Known[Lub.of[a, b]#t[u] =:= Lub.Aux[a, b, u]] should not be null
  }
}
