package fun
package typelevel
package or

import
  list.{
    List,
  },
  interpretation.{
    Interpretation,
  }

/**
  * A runtime interpretation of [[Or]], that comes from evidence that `a` is known.
  * @tparam a type `a` of [[Or]]
  * @tparam b type `b` of [[Or]]
  */
final class OrInterpretationA[
  a: Or.resultOf[a, b]#t: Interpretation.withInOut[ain, aout]#t,
  b: Interpretation.withInOut[bin, bout]#t,
  ain <: List, aout,
  bin <: List, bout,
  rest <: List
] private() extends OrInterpretation[a, b, ain, aout, bin, bout, rest] {

  type Out = aout

  val f: In ⇒ Out =
    ab ⇒ Known[Interpretation.Aux[a, ain, aout]].f(ab.head)

}

/**
  * Provide constructors for [[OrInterpretationA]].
  */
object OrInterpretationA {

  /**
    * Construct an [[OrInterpretationA]] with the given types.
    * @tparam a type `a` of [[Or]]
    * @tparam b type `b` of [[Or]]
    * @tparam rest the "rest" of the input list
    * @return an instance of [[OrInterpretationA]]
    */
  @inline def apply[
    a: Or.resultOf[a, b]#t: Interpretation.withInOut[ain, aout]#t,
    b: Interpretation.withInOut[bin, bout]#t,
    ain <: List, aout, bin <: List, bout,
    rest <: List
  ](): OrInterpretationA[a, b, ain, aout, bin, bout, rest] =
    new OrInterpretationA[a, b, ain, aout, bin, bout, rest]()

}
