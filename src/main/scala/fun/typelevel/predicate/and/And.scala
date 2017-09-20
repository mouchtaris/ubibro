package fun.typelevel.predicate
package and

import known._

/**
  * `And` is evidence that both `a` and `b` are implicitly known.
  *
  * @tparam a type `a` which is "known"
  * @tparam b type `b` which is "known"
  */
trait And[+a, +b] {

  /**
    * The implicitly "known" value for type `a`.
    */
  val a: a

  /**
    * The implicitly "known" value for type `b`.
    */
  val b: b

}

object And {

  /**
    * An implementation of [[And]].
    *
    * @param a type a
    * @param b type b
    * @tparam a type `a` which is "known"
    * @tparam b type `b` which is "known"
    */
  private[this] class impl[a, b](val a: a, val b: b) extends And[a, b]

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
    new impl(a, b)


  /**
    * An implicitly entailed `And` is provided if both `a`
    * and `b` are implicitly known.
    *
    * @tparam a first evidence known
    * @tparam b second evidence known
    * @return an `And` evidence for `a` and `b`
    */
  @inline implicit def andEntails[a: Known, b: Known]: And[a, b] =
    And(Known[a], Known[b])
}