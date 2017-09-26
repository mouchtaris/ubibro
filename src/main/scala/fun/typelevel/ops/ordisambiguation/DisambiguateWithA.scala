package fun.typelevel
package ops
package ordisambiguation

final case class DisambiguateWithA[ora, orb, ifa, ifb]() extends OrInterpretation[ora, orb, ifa, ifb] {

  type Out = ifa

  @inline def apply(implicit ifa: ifa, ifb: ifb): Out =
    ifa

}
