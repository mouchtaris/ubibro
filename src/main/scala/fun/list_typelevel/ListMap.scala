package fun
package list_typelevel

import list._

/**
  * A type-level  map operation on a list, which maps all types in a [[List]] with
  * a type constructor `F[_]`.
  */
trait ListMap[F[_], list <: List] {

  /**
    * The resulting list type, after applied `F` to each type
    * in the list.
    */
  type Out <: List

}

/**
  * Provide implicit resolvers for acquiring a [[ListMap]] for a
  * list.
  */
object ListMap {

  /**
    * The sole instance of this trait, since it's a type-level trait.
    */
  private[this] object instance extends ListMap[typelevel.predicate.Known, Nil]

  /**
    * An auxiliary type, which refines the type `Out` on a [[ListMap]]
    * evidence.
    * @tparam f type constructor applied
    * @tparam list the mapped list
    * @tparam out the resulting list
    */
  type Aux[f[_], list <: List, out <: List] = ListMap[f, list] {
    type Out = out
  }

  /**
    * Construct an instance of [[ListMap]] with the given type arguments.
    * @tparam f the applied type constructor
    * @tparam list the mapped list
    * @tparam out the resulting list type
    * @return a [[ListMap]] instance with the given types
    */
  @inline def apply[f[_], list <: List, out <: List](): Aux[f, list, out] =
    instance.asInstanceOf[Aux[f, list, out]]

  /**
    * Mapping an empty list ([[Nil]]) results in the empty list.
    * The type constructor `F[_]` is never applied to [[Nil]].
    * @tparam f type constructor -- unused
    * @return evidence for mapping a [[Nil]] list
    */
  @inline implicit def nilListMap[f[_]]: Aux[f, Nil, Nil] =
    apply()

  /**
    * Implicit evidence for mapping any other list.
    *
    * This requires evidence for the tail of the list, from which
    * the mapped tail types are known.
    * @param bMap implicitly resolved [[ListMap]] evidence for the tail of the list
    * @tparam f type constructor
    * @tparam a list's head type
    * @tparam b list's tail type
    * @tparam bMapOut the result type of mapping list's tail with type constructor `f[_]``
    * @return evidence for `a :: b`
    */
  @inline implicit def listMap[f[_], a, b <: List, bMapOut <: List](
    implicit
    bMap: Aux[f, b, bMapOut]
  ): Aux[f, a :: b, f[a] :: bMapOut] =
    apply()

}
