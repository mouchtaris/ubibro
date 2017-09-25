package fun.typelevel.predicate
package ops
package ordisambiguation

final case class DisambiguateWithA[ora, orb, ifa, ifb]() extends OrDisambiguation[ora, orb, ifa, ifb] {

  type Out = ifa

  @inline def apply(implicit ifa: ifa, ifb: ifb): Out =
    ifa

}
