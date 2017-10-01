package fun
package interpretation

import
  typelevel._

import
  org.scalatest._

class InterpretationSpec extends FlatSpec with Matchers {

  "Interpretation" should "accept one type parameter" in {
    itsatype[ Interpretation[Int] ] shouldBe OK
  }

  it should "provide an In type" in {
    itsatype[ Interpretation[t]#In forSome { type t } ] shouldBe OK
  }

  it should "provide an Out type" in {
    itsatype[ Interpretation[t]#Out forSome { type t } ] shouldBe OK
  }

  it should "provide an Evidence type alias" in {
    type ev = Any
    Known[ Interpretation[ev]#Evidence =:= ev ] should not be null
  }

  it should "provide a function for runtime behaviour" in {
    Interpretation[Int, Int, Int] { int ⇒ int ⇒ int }.f should not be null
  }

  "Interpretation companion object" should "provide a Aux type alias" in {
    itsatype[ Interpretation.Aux[Int, Int, Int] ] shouldBe OK
  }

  it should "provide a constructor" in {
    Interpretation[Int, Int, Int] { int ⇒ _ ⇒ int } shouldBe a[Interpretation.any]
  }

  "Interpretation.any" should "be a type alias" in {
    itsatype[ Interpretation.any ] shouldBe OK
  }

  "Instances created by the constructor" should "have the specified types" in {
    type ev = Int
    type in = Unit
    type out = String
    val int = Interpretation[ev, in, out] { _ ⇒ _ ⇒ "hello" }
    Known[ int.In =:= in ] should not be null
    Known[ int.Out =:= out ] should not be null
    Known[ int.Evidence =:= ev ] should not be null
  }

  it should "use the given runtime behaviour" in {
    type ev = Int
    type in = String
    type out = Unit
    val f: ev ⇒ in ⇒ out = _ ⇒ _ ⇒ ()
    val intrp = Interpretation(f)
    intrp.f shouldBe f
  }

  "Interpretation.Aux" should "be a type alias for Interpretation" in {
    type ev = Int
    type in = String
    type out = Unit
    type aux = Interpretation.Aux[ev, in, out]
    Known[ aux#Evidence =:= ev ] should not be null
    Known[ aux#In =:= in ] should not be null
    Known[ aux#Out =:= out ] should not be null
  }

}
