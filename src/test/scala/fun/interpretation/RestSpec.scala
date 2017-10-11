package fun
package interpretation

import
  typelevel.{
    itsatype,
    OK,
    Known,
  },
  list.{
    List,
    Nil,
    ::,
  }
import
  org.scalatest._

class RestSpec extends FlatSpec with Matchers {

  "Rest" should "accept a type parameter" in {
    itsatype[ Rest[Int :: Nil] ] shouldBe OK
  }

  it should "require the first type argument to be a list" in {
    "Known[ Rest[Int] ]" shouldNot typeCheck
  }

  it should "be implicitly available for any list" in {
    Known[ Rest[ Int :: String :: Double :: Float :: Short :: Long :: Unit :: Nil ] ] should not be null
  }

  it should "default to Nil when list is unbound" in {
    final case class Spy[rest <: List: Rest]() { type Rest = rest }
    val spy = Spy()
    Known[ spy.Rest =:= Nil] should not be null
  }

}
