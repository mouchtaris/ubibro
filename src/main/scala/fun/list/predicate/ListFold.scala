package fun
package list
package predicate

/**
  * A type-level right-fold operation on the types of a list.
  *
  * The types are folded with a given type-constructor-2, `f`.
  *
  * The right, type fold of a list of types `T1 :: T2 :: ... :: Tn :: Nil`
  * is `f[T1, f[T2, f[..., f[Tn, Nil]]]]`.
  *
  * @tparam f a type-constructor-2
  * @tparam list the list to be folded
  */
trait ListFold[f[_, _], list <: List] {

  /**
    * The resulting type of type-folding the types of this list.
    */
  type Out

  /**
    * Convenience type, which can be used to declare implicit
    * parameters in type parameters.
    *
    * Ex
    * {{{
    *   def anotherWayToPutIt[f[_], list <: List, result: ListFold[f, list]#Result](result: result): result =
    *     result
    * }}}1
    * @tparam out the resulting type `Out`
    */
  final type Result[out] = ListFold.Aux[f, list, out]

}

/**
  * Provide implicit resolutions and constructors for [[ListFold]] evidence.
  */
object ListFold {

  /**
    * The sole instance of [[ListFold]], since it is a type-level trait.
    */
  private[this] object instance extends ListFold[Tuple2, Nil]

  /**
    * A type alias for [[ListFold]], that refines the result output type
    * of [[ListFold]].
    * @tparam f type-constructor-2
    * @tparam list the list being folded
    * @tparam out the result of folding `list` with type constructor `f`
    */
  type Aux[f[_, _], list <: List, out] = ListFold[f, list] {
    type Out = out
  }

  /**
    * A convenience type alias for a conceptual `ListFold[Any, Any]`.
    */
  type any = ListFold[f, list] forSome {
    type f[_, _]
    type list <: List
  }

  /**
    * Construct an instance of evidence for [[ListFold]] with the given
    * type arguments.
    * @tparam f the type constructor applied to list
    * @tparam list the list
    * @tparam out the result type of folding types in `list` with type constructor `f`
    * @return evidence with the given types
    */
  @inline def apply[f[_, _], list <: List, out](): Aux[f, list, out] =
    instance.asInstanceOf[Aux[f, list, out]]

  /**
    * Implicit evidence for folding a list with a single element type.
    *
    * This list is essentially not folded, and the single element type
    * is the result of this operation.
    * @tparam f the type constructor
    * @tparam a the single type in the list
    * @return evidence for folding `a :: Nil`, whose output type is also `a`
    */
  @inline implicit def singleItemLisFold[f[_, _], a]: Aux[f, a :: Nil, a] =
    apply()

  /**
    * Implicit evidence for folding a [[list.List]].
    * @param bFold evidence for folding the tail of the given list type
    * @tparam f the type constructor
    * @tparam a the list's head's type
    * @tparam b the list's tail's type
    * @return evidence for `a :: b`
    */
  @inline implicit def listFold[f[_, _], a, b <: List](
    implicit
    bFold: ListFold[f, b]
  ): Aux[f, a :: b, f[a, bFold.Out]] =
    apply()

}