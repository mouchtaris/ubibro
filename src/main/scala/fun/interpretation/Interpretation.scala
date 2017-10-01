package fun
package interpretation

/**
  * An Interpretation of a type `T` is a function that can interpret an
  * arbitrary input type to an arbitrary output type, at runtime,
  * given evidence of the type `T`.
  *
  * Interpretations can be used to implement structured runtime behaviour,
  * that composes nicely, cleanly and "magically" from well defined
  * generic interpreters.
  * @tparam T a type for which the interpretation is defined
  */
trait Interpretation[T] {

  /**
    * The input type for this interpretation
    */
  type In

  /**
    * The output type for this interpretation
    */
  type Out

  /**
    * The evidence type of this interpretation
    */
  final type Evidence = T

  /**
    * The runtime behaviour of this interpretation.
    */
  val f: Evidence ⇒ In ⇒ Out

  /**
    * Runtime interpretation for `In` into `Out`, given `Evidence`
    * @param in input
    * @param evidence evidence
    * @return output
    */
  @inline final def apply(in: In)(implicit evidence: Evidence): Out =
    f(evidence)(in)

}

/**
  * Provide constructors and implicit instances for [[Interpretation]].
  */
object Interpretation {

  /**
    * An auxiliary type alias, that refined the input and output types
    * of [[Interpretation]].
    * @tparam t evidence type
    * @tparam in input type
    * @tparam out output type
    */
  type Aux[t, in, out] = Interpretation[t] {
    type In = in
    type Out = out
  }

  /**
    * A convenience type alias for a conceptual `Interpretation[Any]` type
    */
  type any = Interpretation[_]

  /**
    * An implementation of [[Interpretation]]
    * @param f the runtime behaviour
    * @tparam t evidence type
    * @tparam in input type
    * @tparam out output type
    */
  private[this] class impl[t, in, out](val f: t ⇒ in ⇒ out) extends Interpretation[t] {
    type In = in
    type Out = out
  }

  /**
    * Construct an instance of [[Interpretation]] with the given types and arguments.
    * @param f runtime behaviour of this interpretation
    * @tparam t type of evidence
    * @tparam in input type
    * @tparam out output type
    * @return an [[Interpretation]] for `t`
    */
  @inline def apply[t, in, out](f: t ⇒ in ⇒ out): Aux[t, in, out] =
    new impl[t, in, out](f)

}
