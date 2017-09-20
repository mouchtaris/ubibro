package fun.typelevel.predicate.or

/**
  * Provides implicit entailment of an [[Or]] through a known `a`.
  *
  * This trait is used to introduce implicit resolution order.
  */
trait OrEntailmentThroughA {

  /**
    * Provide evidence that `Or[a, b]`, because `a` is known.
    * @param a `a`'s evidence
    * @tparam a type a
    * @tparam b type b
    * @return evidence that `a OR b`
    */
  @inline final def orEntailedThroughA[a, b](implicit a: a): Or[a, b] =
  Or.left(a)

}
