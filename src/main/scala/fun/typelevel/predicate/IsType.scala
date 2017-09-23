package fun
package typelevel
package predicate

/**
  * Evidence that proves type equality between types `a` and `b`.
  * @tparam a type a
  * @tparam b type b
  */
trait IsType[a, b] {
  implicit val evidence: a =:= b
}

/**
  * Provide constructors and implicit resolvers for evidence of type [[IsType]].
  */
object IsType {

  /**
    * The sole instance of this evidence, since it's a type-level type.
    */
  private[this] class impl[a, b](val evidence: a =:= b) extends IsType[a, b]

  /**
    * Convenience type alias for a conceptual `IsType[Any, Any]`.
    */
  type any = IsType[_, _]

  /**
    * Convenience type alias to allow writing implicit requirements, like
    * {{{
    *   def isItInt[t: IsType.is[Int]#t](t: t): t.type = t
    * }}}
    * @tparam a type to check equality against
    * @tparam b type to check its equality against a
    */
  type is[a] = {
    type t[b] = IsType[a, b]
  }

  /**
    * Construct an instance of [[IsType]] with the given types.
    * @tparam a type a
    * @tparam b type b
    * @return a new [[IsType]] instance
    */
  @inline def apply[a, b](evidence: a =:= b): IsType[a, b] =
    new impl(evidence)

  /**
    * Implicitly provide evidence of the type equality between `a` and `b`,
    * if there is a scala provided [[=:=]] evidence implicitly known.
    * @param ev scala type equality evidence
    * @tparam a type a
    * @tparam b type b
    * @return evidence for the type equality of `a` and `b`
    */
  @inline implicit def equiv[a, b](implicit ev: a =:= b): IsType[a, b] =
    apply(implicitly)

}
