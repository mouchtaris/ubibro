package incubator

import
  org.scalatest._

class MainSpec extends FlatSpec with Matchers {

  "incubator.Main" should "run ok" in {
    incubator.Main.main(Array.empty)
    0
  }

}
