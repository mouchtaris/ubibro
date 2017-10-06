package fun
package typelevel
package or

import
  list.{
    List
  },
  list.typelevel.{
    Concat
  },
  interpretation.{
    Interpretation
  }

/**
  * A runtime interpretation of [[Or]], that comes from evidence that `orb` is known.
  * @tparam a type `a` of [[Or]]
  * @tparam b type `b` of [[Or]]
  */
final class OrInterpretationB[
  a: Interpretation.withInOut[ain, aout]#t,
  b: Or.resultOf[a, b]#t: Interpretation.withInOut[bin, bout]#t,
  ain <: List: Concat.to[bin]#t, aout,
  bin <: List, bout,
  rest <: List
] private() extends OrInterpretation[a, b, ain, aout, bin, bout, rest] {

  type Out = bout

  val f: In ⇒ Out =
    ab ⇒ Known[Interpretation.Aux[b, bin, bout]].f(ab.tail.head)

}

/**
  * Provide constructors for [[OrInterpretationB]].
  */
object OrInterpretationB {

  /**
    * Construct an [[OrInterpretationB]] with the given types.
    * @tparam a type `a` of [[Or]]
    * @tparam b type `b` of [[Or]]
    * @tparam rest the "rest" of the input list
    * @return an instance of [[OrInterpretationB]]
    */
  @inline def apply[
    a: Interpretation.withInOut[ain, aout]#t,
    b: Or.resultOf[a, b]#t: Interpretation.withInOut[bin, bout]#t,
    ain <: List: Concat.to[bin]#t, aout,
    bin <: List, bout,
    rest <: List
  ](): OrInterpretationB[a, b, ain, aout, bin, bout, rest] =
    new OrInterpretationB[a, b, ain, aout, bin, bout, rest]()

}
