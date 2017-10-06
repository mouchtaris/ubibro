package fun
package list
package typelevel

import
  org.scalatest._,
  fun.typelevel.{
    Known,
    itsatype,
    OK
  }

class ConcatSpec extends FlatSpec with Matchers {

  type a1 = Int
  type a2 = Unit
  type b1 = String
  type a = a1 :: a2 :: Nil
  type b = b1 :: Nil
  type out = Unit :: Nil

  "Concat" should "accept two type parameters" in {
    itsatype[ Concat[Nil, Nil] ] shouldBe OK
  }

  it should "require the first argument to be a list" in {
    "Known[ Concat[Int, Nil] ]" shouldNot typeCheck
  }

  it should "requirethe second argument to be a list" in {
    "Known[ Concat[Nil, Int] ]" shouldNot typeCheck
  }

  "Concat.Aux" should "be a type alias for Concat" in {
    Known[ Concat.Aux[a, b, out] =:= Concat[a, b] { type Out = out } ] should not be null
  }

  "Concat.any" should "hold any concat type" in {
    Known[ Concat.Aux[a, b, out] <:< Concat.any ] should not be null
  }

  "Concat() constructor" should "return a concat instance" in {
    Concat[a, b, out]() shouldBe a[Concat.any]
  }

  it should "respect the \"out\" type" in {
    val c = Concat[a, b, out]()
    Known[ c.Out =:= out ] should not be null
  }

  "Concat[Nil, Nil]" should "have output Nil" in {
    val c = Known[ Concat[Nil, Nil] ]
    Known[ c.Out =:= Nil ]
  }

  "Concat[Nil, b]" should "have output b" in {
    val c = Known[ Concat[Nil, b] ]
    Known[ c.Out =:= b ] should not be null
  }

  "Concat[a, Nil]" should "have output a" in {
    val c = Known[ Concat[a, Nil] ]
    Known[ c.Out =:= a ] should not be null
  }

  "Concat[a, b]" should "have output a1 :: a2 :: b1 :: Nil" in {
    val c = Known[ Concat[a, b] ]
    Known[ c.Out =:= (a1 :: a2 :: b1 :: Nil) ] should not be null
  }

}
