package fun
package list
package typelevel

import
  concat.{
    ConcatReverseInterpretation,
  }

/**
  * A type-level operation which concatenates two list types into one.
  * @tparam a list type in the front
  * @tparam b list type at the back
  */
trait Concat[a <: List, b <: List] {

  /**
    * The resulting type of concatenating list types `a` and `b`
    */
  type Out <: List

}

/**
  * Provides constructors and implicit resolution for type constructor [[Concat]].
  */
object Concat {

  /**
    * The sole instance of this type, since it's a type-level marker type.
    */
  private[this] object Instance extends Concat[Nil, Nil] {
    type Out = Nil
  }

  /**
    * Auxiliary type alias, which refines the output, result type `Out`.
    * @tparam a [[Concat]] type parameter `a`
    * @tparam b [[Concat]] type parameter `b`
    * @tparam out [[Concat]] output type `Out`
    */
  type Aux[a <: List, b <: List, out <: List] = Concat[a, b] {
    type Out = out
  }

  /**
    * Provide a conceptual `any` type for [[Concat]]
    */
  type any = Aux[_ <: List, _ <: List, _ <: List]

  type resultOf[a <: List, b <: List] = {
    type t[r <: List] = Aux[a, b, r]
  }

  /**
    * Construct an instance of [[Concat]] with the given types.
    * @tparam a [[Concat]] type parameter `a`
    * @tparam b [[Concat]] type parameter `b`
    * @tparam out [[Concat]] output type `Out`
    * @return a [[Concat]] instance with the given types
    */
  @inline def apply[a <: List, b <: List, out <: List](): Aux[a, b, out] =
    Instance.asInstanceOf[Aux[a, b, out]]

  /**
    * Implicit evidence about concatenating an empty list with
    * an arbitrary list `b`. The result is always `b`.
    * @tparam b a list type
    * @return concat evidence with `Out = b`
    */
  @inline implicit def prependNil[b <: List]: Aux[Nil, b, b] =
    apply()

  /**
    * Implicit evidence about concatenating an arbitrary list `h :: a`
    * to an arbitrary list `b`.
    * @param concat evidence for the concatenation of `a` and `b`
    * @tparam h type of head of the front-list
    * @tparam a type of tail of the front-list
    * @tparam b type of the back-list
    * @return concat evidence for the concatenation of `h :: a` and `b`
    */
  @inline implicit def prependList[h, a <: List, b <: List](
    implicit
    concat: Concat[a, b]
  ): Aux[h :: a, b, h :: concat.Out] =
    apply()

  def unapply[
    a <: List,
    b <: List,
    ab <: List: Concat.resultOf[a, b]#t
  ](
    list: ab
  )(
    implicit
    concat: ConcatReverseInterpretation[a, b, ab]
  )
  : Option[a :: b :: Nil] =
    Some(concat(list))

}
