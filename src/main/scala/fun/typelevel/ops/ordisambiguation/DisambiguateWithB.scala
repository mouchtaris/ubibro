package fun.typelevel
package ops
package ordisambiguation

final case class DisambiguateWithB[ora, orb, ifa, ifb]() extends OrInterpretation[ora, orb, ifa, ifb] {

  type Out = ifb

  @inline def apply(implicit ifa: ifa, ifb: ifb): Out =
    ifb

}
