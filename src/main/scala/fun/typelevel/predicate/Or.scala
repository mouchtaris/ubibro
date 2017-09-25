package fun
package typelevel
package predicate

import
  scala.annotation.implicitNotFound
import
  fun.typelevel.predicate.or.{ OrEntailments }

/**
  * Implicit evidence that either `a` or `b` is implicitly known.
  *
  * @tparam a type a
  * @tparam b type b
  */
@implicitNotFound("It is not known that ${a} OR ${b}")
trait Or[a, b] {

  /**
    * LUB evidence for `a` and `b`
    */
  final val lub = Known[Lub[a, b]]

  /**
    * An alias to `a`'s and `b`'s LUB
    */
  final type LUB = lub.Out

  /**
    * An alias to type `a`
    */
  final type A = a

  /**
    * An alias to type `b`
    */
  final type B = b

  /**
    * The type which makes this [[Or]] true. It conforms to `LUB`.
    */
  type Out <: LUB

  /**
    * The implicitly resolved instance of either `a` or `b`
    */
  val evidence: Out

}

/**
  * Provide implicit instances and construction for [[Or]] evidence.
  */
object Or extends AnyRef
  with OrEntailments
{

  /**
    * An implementation of [[Or]].
    * @param evidence the evidence of the implicitly known instance
    * @tparam a type a
    * @tparam b type b
    */
  private[this] class impl[+a, +b, t](val evidence: t) extends Or[a, b] {
    type Out = t
  }

  /**
    * A convenience type which refines the output result type `Out`.
    * @tparam a type a
    * @tparam b type b
    * @tparam t type t that makes it true
    */
  type Aux[a, b, t] = Or[a, b] {
    type Out = t
  }

  /**
    * A convenience type alias, that can be used in declaring implicit parameters.
    *
    * Ex.
    * {{{
    *   def itsTheOrResult[a, b, or: Or.resultOf[a, b]#t]: Or.Aux[a, b, or] = implicitly
    * }}}
    * @tparam a
    * @tparam b
    */
  type resultOf[a, b] = {
    type t[out] = Aux[a, b, out]
  }

  /**
    * Construct a new evidence of type `Or[a, b]` from the given evidence.
    * @param evidence the evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def apply[a, b, t](evidence: t): Aux[a, b, t] =
    new impl(evidence)

  /**
    * An [[Or]] that is true because of `a`.
    * @param a `a`'s evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def left[a, b](a: a): Aux[a, b, a] =
    apply(a)

  /**
    * An `Or` that is true because of `b`.
    * @param b `b`s evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def right[a, b](b: b):Aux[a, b, b] =
    apply(b)

  /**
    * Get the implicit evidence that `Or[a, b]`, if it is known.
    *
    * @param or implicitly resolved
    * @tparam a type a
    * @tparam b type b
    * @return the evidence
    */
  @inline def apply[a, b](implicit or: Or[a, b]): or.type =
    or

}
