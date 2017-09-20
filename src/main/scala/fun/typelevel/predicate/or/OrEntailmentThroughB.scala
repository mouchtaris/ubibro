package fun.typelevel.predicate.or

/**
  * Provides implicit entailment of an [[Or]] through a known `b`.
  *
  * This trait is used to introduce implicit resolution order.
  */
trait OrEntailmentThroughB {

  /**
    * Provide evidence that `Or[a, b]`, because `b` is known.
    * @param b `b`'s evidence
    * @tparam a type a
    * @tparam b type b
    * @return evidence that `a OR b`
    */
  @inline final def orEntailedThroughB[a, b](implicit b: b): Or[a, b] =
    Or.right(b)

}
