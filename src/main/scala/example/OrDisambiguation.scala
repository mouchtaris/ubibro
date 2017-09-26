package example

import
  fun.typelevel._,
  ops._

object OrDisambiguation {

  def main(args: Array[String]): Unit = {

    case object a
    implicit object b

    case object ifa { object onlya }
    case object ifb { object onlyb }

    Known[ OrDisambiguation[a.type, b.type, ifa.type, ifb.type] ]
      .apply(ifa, ifb)
      .onlyb // inferred at compile that Or[a, b] is satisfied by `b`,
    // and the return type of apply() if ifb.type

  }

}
