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
  * A concat interpretation is able to quickly drop the initial part `a` of the list,
  * returning `b` directly. Given enough compiler optimizations, this
  * is essentially a statement equivalent to `out.tail.tail.tail.tail ...`.
  *
  * The input type is defined as an explicit parameter. It should at least begin
  * with the concatenation of `a` and `b`. This check is enforced at
  * creation time of a [[ConcatInterpretation]].
  *
  * @tparam a a list type `a`
  * @tparam b a list type `b`
  * @tparam in the input list type of this interpretation -- at least `Concat[Concat[a, b].Out, Rest]`.
  */
sealed trait ConcatInterpretation[a <: List, b <: List, in <: List] extends Interpretation[Concat[a, b]] {

  /**
    * The input type for this interpretation.
    *
    * Defined by type parameter `in`.
    *
    * This type should be at least `Concat[Concat[a, b].Out, Rest]`, something that
    * cannot be expressed with type conformance statements. It is enforced at creation
    * time through implicit bounds.
    */
  final type In = in

  /**
    * The output type for this interpretation.
    *
    * Defined as `b`
    */
  final type Out = b

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
    val f: in ⇒ b
  ) extends AnyRef
    with ConcatInterpretation[a, b, in]

  /**
    * Convenience type alias for declaring implicit parameters
    *
    * {{{
    *   def nothingUseful[a <: List, b <: List, in <: List: ConcatInterpretation.resultOf[a, b]#t] = ????
    * }}}
    * @tparam a list type `a`
    * @tparam b list type `b`
    */
  type inputOf[a <: List, b <: List]  = {
    type t[in <: List] = ConcatInterpretation[a, b, in]
  }

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
  implicit def apply[a <: List, b <: List, in <: List](f: in ⇒ b): ConcatInterpretation[a, b, in] =
    new impl[a, b, in](f)

}
