package fun.typelevel
package interpreter
package or_interpretation

final case class InterpretWithA[ora, orb, ifa, ifb]() extends OrInterpretation[ora, orb, ifa, ifb] {

  type Out = ifa

  @inline def apply(implicit ifa: ifa, ifb: ifb): Out =
    ifa

}
