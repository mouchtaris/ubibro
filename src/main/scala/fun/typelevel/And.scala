package fun
package typelevel

import
  scala.annotation.implicitNotFound

/**
  * `And` is evidence that both `a` and `b` are implicitly known.
  *
  * @tparam a type `a` which is "known"
  * @tparam b type `b` which is "known"
  * @param a the implicitly "known" value for type `a`
  * @param b the implicitly "known" value for type `b`
  */
@implicitNotFound("It is not known that ${a} AND ${b}")
sealed abstract class And[+a, +b](

  val a: a,

  val b: b

)

object And {

  /**
    * Create a new instance of `And[a, b]`.
    *
    * @param a the first known type's instance
    * @param b the second known type's instance
    * @tparam a the first known type
    * @tparam b the second known type
    * @return a new `And[a, b]` instance
    */
  @inline def apply[a, b](a: a, b: b): And[a, b] =
    new And[a, b](a, b) { }


  /**
    * An implicitly entailed `And` is provided if both `a`
    * and `b` are implicitly known.
    *
    * @tparam a first evidence known
    * @tparam b second evidence known
    * @return an `And` evidence for `a` and `b`
    */
  @inline implicit def andEntailed[a <: AnyRef, b <: AnyRef](implicit a: a, b: b): And[a, b] =
    And(a, b)

  /**
    * Get the implicit evidence that `And[a, b]`, if it is known.
    *
    * @param and implicitly resolved
    * @tparam a type a
    * @tparam b type b
    * @return an evidence instance
    */
  @inline def apply[a <: AnyRef, b <: AnyRef](implicit and: And[a, b]): and.type =
    and

}
