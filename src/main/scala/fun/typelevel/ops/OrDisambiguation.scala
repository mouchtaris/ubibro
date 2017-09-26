package fun
package typelevel
package ops

/**
  * A disambiguation functor for an [[Or]] evidence.
  *
  * Depending on whether [[Or]] is satisfied by `a` or `b`,
  * implicit resolution of this functor will yield a functor returning either
  * `ifa` or `ifb`, respectively, when both instances are provided.
  *
  * @tparam a type `a` in [[Or]]
  * @tparam b type `b` in [[Or]]
  * @tparam ifa type of result if `a` is true
  * @tparam ifb type of result if `b` is true
  */
trait OrDisambiguation[a, b, ifa, ifb] {

  /**
    * The output type, either `ifa` or `ifb`
    */
  type Out

  /**
    * Given two objects, `ifa` and `ifb`, return the former
    * if `Or` is satisfied by `a`, or return the latter otherwise.
    * @param ifa result if `Or` is satisfied by `a`
    * @param ifb result if `Or` is satisfied by `b`
    * @return
    */
  def apply(implicit ifa: ifa, ifb: ifb): Out

}

/**
  * Provide constructors and implicit resolutions for [[OrDisambiguation]].
  */
object OrDisambiguation {

  import
    ordisambiguation.{
      DisambiguateWithA,
      DisambiguateWithB,
    }

  /**
    * Convenience type alias, which refines the output result type `Out`.
    * @tparam a type `a` in [[Or]]
    * @tparam b type `b` in [[Or]]
    * @tparam ifa result type if `a`
    * @tparam ifb result type if `b`
    * @tparam out result type
    */
  type Aux[a, b, ifa, ifb, out] = OrDisambiguation[a, b, ifa, ifb] {
    type Out = out
  }

  /**
    * Implicitly provide an "A" disambugator if [[Or]] is satisfied by `a`.
    * @tparam ora type `a` of [[Or]]
    * @tparam orb type `b` of [[Or]]
    * @tparam orOut the satisfying type of [[Or]]
    * @tparam ifa type of object if [[Or]] is satisfied by `a`
    * @tparam ifb type of object if [[Or]] is satisfied by `b`
    * @return a disambiguator that returns `a`
    */
  @inline implicit def disambiguateWithA[
    ora, orb, orOut: Or.resultOf[ora, orb]#t: IsType.is[ora]#t,
    ifa, ifb
  ]: Aux[ora, orb, ifa, ifb, ifa] =
    DisambiguateWithA()

  /**
    * Implicitly provide a "B" disambugator if [[Or]] is satisfied by `b`.
    * @tparam ora type `a` of [[Or]]
    * @tparam orb type `b` of [[Or]]
    * @tparam orOut the satisfying type of [[Or]]
    * @tparam ifa type of object if [[Or]] is satisfied by `a`
    * @tparam ifb type of object if [[Or]] is satisfied by `b`
    * @return a disambiguator that returns `b`
    */
  @inline implicit def disambiguateWithB[
    ora, orb, orOut: Or.resultOf[ora, orb]#t: IsType.is[orb]#t,
    ifa, ifb
  ]: Aux[ora, orb, ifa, ifb, ifb] =
    DisambiguateWithB()

}
