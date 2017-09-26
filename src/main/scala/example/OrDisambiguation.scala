package example

import
  fun.typelevel._,
  interpreter._

object OrDisambiguation {

  def main(args: Array[String]): Unit = {

    case object a
    case object b

    case object ifa { def onlya(): Unit = () }
    case object ifb { def onlyb(): Unit = () }

    {
      implicit val ib = b
      Known[ OrInterpretation[a.type, b.type, ifa.type, ifb.type] ]
        .apply(ifa, ifb)
        .onlyb() // inferred at compile that Or[a, b] is satisfied by `b`,
      // and the return type of apply() if ifb.type
    }

    {
      implicit val ia = a
      Known[ OrInterpretation[a.type, b.type, ifa.type, ifb.type] ]
        .apply(ifa, ifb)
        .onlya()
    }

  }

}
