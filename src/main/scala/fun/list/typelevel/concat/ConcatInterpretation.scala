package fun
package list
package typelevel
package concat

import
  interpretation.{
    Interpretation,
  }

/**
  * A [[Concat]] interpretation.
  *
  * A concat interpretation at runtime is able to dis-join two list, `a` and `b`.
  *
  * The input type is defined as an explicit parameter.
  *
  * @tparam a a list type `a`
  * @tparam b a list type `b`
  * @tparam in the input list type of this interpretation
  */
trait ConcatInterpretation[a <: List, b <: List, in <: List] extends Interpretation[Concat[a, b]] {

  /**
    * The input type for this interpretation.
    *
    * Defined by type parameter `in`.
    */
  final type In = in

  /**
    * The output type for this interpretation.
    *
    * Defined as `a :: b :: Nil`.
    */
  final type Out = a :: b :: Nil

  /**
    * The `a` list type for this [[Concat]]
    */
  final type A = a

  /**
    * The `b` list type for this [[Concat]]
    */
  final type B = b

}

/**
  * Provide constructors and implicit resolution for this interpreter.
  */
object ConcatInterpretation {

  /**
    * An implementation of [[ConcatInterpretation]]
    * @param f runtime behaviour
    * @tparam a a list type `a`
    * @tparam b a list type `b`
    * @tparam in the input list type of this interpretation
    */
  private[this] final class impl[a <: List, b <: List, in <: List](
    val f: in ⇒ a :: b :: Nil
  ) extends AnyRef
    with ConcatInterpretation[a, b, in]

  /**
    * Construct an instance of this interpreter.
    *
    * This constructor is also available as an implicit conversion
    * from a function `f`.
    *
    * @param f runtime behaviour
    * @tparam a type param `a`
    * @tparam b type param `b`
    * @tparam in type param `in`
    * @return an interpreter
    */
  implicit def apply[a <: List, b <: List, in <: List](f: in ⇒ a :: b :: Nil): ConcatInterpretation[a, b, in] =
    new impl[a, b, in](f)

  /**
    * Implicit interpretation for deconstructing a list with Nil.
    * @tparam b a list type
    * @return an interpreter
    */
  implicit def concatInterpretationNil[b <: List: Concat.resultOf[Nil, b]#t]: ConcatInterpretation[Nil, b, b] =
    // Implicit construction
    (b: b) ⇒
      Nil :: b :: Nil

  /**
    * Implicit interpretation for any other list concatenation `h :: t :: b`.
    *
    * This requires implicit evidence for the interpretation of the
    * tail of the list,`t :: b`.
    *
    * @param tbconcat interpretation for the concatenation of `t` and `b`
    * @tparam h head type
    * @tparam t tail type of list `a`
    * @tparam b list `b`
    * @tparam tb lists `b` and `t` concatenated
    * @return an interpreter
    */
  implicit def concatInterpretationList[
    h,
    t <: List,
    b <: List,
    tb <: List: Concat.resultOf[t, b]#t
  ](
    implicit
    tbconcat: ConcatInterpretation[t, b, tb]
  ): ConcatInterpretation[h :: t, b, h :: tb] =
    ConcatInterpretation {
      case h :: Concat(t :: b :: Nil) ⇒
        (h :: t) :: b :: Nil
    }

}
