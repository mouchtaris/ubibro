package fun
package list
package predicate

/**
  * A type-level  map operation on a list, which maps all types in a [[list.List]] with
  * a type constructor `F[_]`.
  */
trait ListMap[F[_], list <: List] {

  /**
    * The resulting list type, after applied `F` to each type
    * in the list.
    */
  type Out <: List

  /**
    * Convenience type, which can be used to declare implicit
    * parameters in type parameters.
    *
    * Ex
    * {{{
    *   def anotherWayToPutIt[f[_], list <: List, result: ListMap[f, list]#Result](result: result): result =
    *     result
    * }}}1
    * @tparam out the resulting type `Out`
    */
  final type Result[out <: List] = ListMap.Aux[F, list, out]

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
    * A convenience type alias for a conceptual `ListMap[Any, Any]`.
    */
  // TODO spec
  type any = ListMap[f, list] forSome {
    type f[_]
    type list <: List
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
    * Mapping an empty list ([[list.Nil]]) results in the empty list.
    * The type constructor `F[_]` is never applied to [[list.Nil]].
    * @tparam f type constructor -- unused
    * @return evidence for mapping a [[list.Nil]] list
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
  @inline implicit def listMap[f[_], a, b <: List](
    implicit
    bMap: ListMap[f, b]
  ): Aux[f, a :: b, f[a] :: bMap.Out] =
    apply()

}
