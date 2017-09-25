package fun
package list
package predicate

import
  typelevel.predicate._

/**
  * Evidence singigying the equivalence of two list types.
  *
  * Two list types are equivalent when they contain the same
  * types, in any amount of duplication and any reordering.
  *
  * Formally, two list types are equivalent if they both are
  * a [[Subset]] of each other.
  *
  * @tparam a a list type
  * @tparam b a list type
  */
sealed trait Equivalent[a <: List, b <: List] {

  /**
   * Type of evidence provided by this evidence.
   */
  final type Evidence = Subset[a, b] `And` Subset[b, a]

  /**
   * Implicit evidence provided by this evidence.
   */
  val evidence: Evidence

}

/**
  * Provide constructors and implicit resolutions for evidence
  * of type [[Equivalent]].
  */
object Equivalent {

  type any = Equivalent[_ <: List, _ <: List]

  /**
    * Convenience alias that allows specifying [[Equivalent]]
    * implicit parameters.
    *
    * Ex.
    * {{{
    *  def equiv[a <: List, b <: List: Equivalent.to[a]#t]: Int = 42
    * }}}
    */
  type to[a <: List] = {
    type t[b <: List] = Equivalent[a, b]
  }

  /**
    * The sole instance of [[Equivalent]], since this is a type-level
    * type.
    */
  private[this] class impl[a <: List, b <: List](val evidence: Equivalent[a, b]#Evidence) extends Equivalent[a, b]

  /**
    * Construct an instance of [[Equivalent]] with the given types.
    * @tparam a a list type
    * @tparam b a list type
    * @return a instance of [[Equivalent]]
    */
  @inline def apply[a <: List, b <: List](evidence: Equivalent[a, b]#Evidence): Equivalent[a, b] =
    new impl(evidence)

  /**
    * Implicitly provide evidence for [[Equivalent]].
    * @tparam a a list type equivalent to list type `b`
    * @tparam b a list type equivalent to list type `a`
    * @return evidence for the equivalence of `a` and `b`
    */
  @inline implicit def listEquivalent[a <: List, b <: List](
    implicit
    evidence: Subset[a, b] `And` Subset[b, a]
  ): Equivalent[a, b] =
    apply(evidence)

}

