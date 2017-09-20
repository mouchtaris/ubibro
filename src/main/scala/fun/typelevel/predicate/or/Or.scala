package fun.typelevel.predicate.or

/**
  * Implicit evidence that either `a` or `b` is implicitly known.
  *
  * @tparam a type a
  * @tparam b type b
  */
trait Or[+a, +b] {

  /**
    * The implicitly resolved instance of either `a` or `b`.
    */
  val evidence: Either[a, b]

}

/**
  * Provide implicit instances and construction for [[Or]] evidence.
  */
object Or extends AnyRef
  with OrEntailmentThroughB
  with OrEntailmentThroughA
{

  /**
    * An implementation of [[Or]].
    * @param evidence the evidence of the implicitly known instance
    * @tparam a type a
    * @tparam b type b
    */
  private[this] class impl[+a, +b](val evidence: Either[a, b]) extends Or[a, b]

  /**
    * Construct a new evidence of type `Or[a, b]` from the given evidence.
    * @param evidence the evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def apply[a, b](evidence: Either[a, b]): Or[a, b] =
    new impl(evidence)

  /**
    * Alias for `Or(Left(a))`
    * @param a `a`'s evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def left[a, b](a: a): Or[a, b] =
    apply(Left(a))

  /**
    * Alias for `Or(Right(b))`.
    * @param b `b`s evidence
    * @tparam a type a
    * @tparam b type b
    * @return a new evidence instance
    */
  @inline def right[a, b](b: b): Or[a, b] =
    apply(Right(b))

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
