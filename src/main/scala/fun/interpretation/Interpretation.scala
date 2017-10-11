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
  * type parameter. This allows arbitrary input for interpreters to be
  * combined in a single [[List]], from which sub-interpreters extract
  * their part of input.
  *
  * `rest` is bound by [[Rest]], which makes it be [[Nil]] by default,
  * but can also be any other [[List]] type. This way, top-level interpreters
  * can request sub-interpreters that accept lists with the "rest" of
  * useless input appended at the end. All this is thanks to
  * [[list.typelevel.Concat]] and [[list.typelevel.concat.ConcatInterpreter]]
  * magic.
  *
  * @tparam t a type for which the interpretation is defined
  */
trait Interpretation[t] {

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
  final type T = t

  /**
    * The runtime behaviour of this interpretation.
    *
    * The reason this is kept as a function is for easier
    * subclassing. Runtime behaviour can be abstracted as a
    * function parameter. Otherwise, every subclass implementation
    * would need to define a lot of boilerplate, meaningless classes,
    * or pollute the byte-code with a lot of inline classes.
    */
  val f: In ⇒ Out

  /**
    * Runtime interpretation for `In` into `Out`, given evidence type `t`.
    * @param in input
    * @return output
    */
  @inline final def apply(in: In): Out =
    f(in)

  /**
    * Every interpreter can act as a de-constructor as well. This is
    * useful when extracting arguments for sub-interpreters, as it
    * allows for the sub-interpreter result to be incorporated in a
    * patter matching statement directly.
    *
    * Ex.
    * {{{
    *   trait Banana
    *
    *   object BananaInterpreter extends Interpreter[Banana] {
    *     type In = Int :: Nil
    *     type Out = String
    *     val f: In ⇒ Out = _.toString
    *    }
    *
    *    val BananaInterpreter(str) = 12 :: Nil
    *    println(str)  // ⇒ 12
    * }}}
    * @param in interpreter input
    * @return interpreter output (always)
    */
  @inline final def unapply(in: In): Option[Out] =
    Some(apply(in))

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
    * Convenience type alias for specifying implicit bounds.
    *
    * {{{
    *   def something[a: Interpreter.withInOut[Int, String]#t](in: Int): String =
    *     Known[Interpreter.Aux[a, Int, String]].f(in)
    * }}}
    * @tparam in input type
    * @tparam out output type
    */
  type withInOut[in, out] = {
    type t[T] = Aux[T, in, out]
  }

  /**
    * An implementation of [[Interpretation]]
    * @param f the runtime behaviour
    * @tparam t evidence type
    * @tparam in input type
    * @tparam out output type
    */
  private[this] class impl[t, in <: List, out](
    val f: in ⇒ out
  ) extends Interpretation[t] {
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
