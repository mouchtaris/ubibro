package fun
package interpretation

import
  list.{
    List,
  }

/**
  * An Interpretation of a type `T` is a function that can interpret an
  * arbitrary input type to an arbitrary output type, at runtime,
  * given evidence of the type `T`.
  *
  * Interpretations can be used to implement structured runtime behaviour,
  * that composes nicely, cleanly and "magically" from well defined
  * generic interpreters. It is essentially a way of implementing AST
  * interpretation through scala implicits.
  *
  * All interpreters define their input in the form of a list in type `In`,
  * which is then followed by an arbitrary "tail", defined by the `rest`
  * type parameter.
  * @tparam T a type for which the interpretation is defined
  */
trait Interpretation[T] {

  /**
    * The input type for this interpretation
    */
  type In <: List

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
  val f: In ⇒ Out

  /**
    * Runtime interpretation for `In` into `Out`, given `Evidence`
    * @param in input
    * @return output
    */
  @inline final def apply(in: In): Out =
    f(in)

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
  type Aux[t, in <: List, out] = Interpretation[t] {
    type In = in
    type Out = out
  }

  /**
    * A convenience type alias for a conceptual `Interpretation[Any]` type
    */
  type any = Interpretation[_]

  type withInOut[in <: List, out] = {
    type t[T] = Aux[T, in, out]
  }

  /**
    * An implementation of [[Interpretation]]
    * @param f the runtime behaviour
    * @tparam t evidence type
    * @tparam in input type
    * @tparam out output type
    */
  private[this] class impl[t, in <: List, out](val f: in ⇒ out) extends Interpretation[t] {
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
  @inline def apply[t, in <: List, out](f: in ⇒ out): Aux[t, in, out] =
    new impl[t, in, out](f)

}
