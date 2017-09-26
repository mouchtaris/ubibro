package fun.typelevel
package interpreter
package or_interpretation

final case class DisambiguateWithB[ora, orb, ifa, ifb]() extends OrInterpretation[ora, orb, ifa, ifb] {

  type Out = ifb

  @inline def apply(implicit ifa: ifa, ifb: ifb): Out =
    ifb

}
