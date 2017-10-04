package fun
package typelevel
package or

import
  list.{
    List,
    ::,
  },
  interpretation.{
    Interpretation,
  }

/**
  * An interpretation of [[Or]].
  *
  * A runtime interpretation of [[Or]] accepts two instances, of types `a` and `b`,
  * and returns either `a` or `b`, depending on which type makes this [[Or]] evidence come true.
  * @param interpa An interpretation of type `a`
  * @param interpb An interpretaion of type `b`
  * @tparam a [[Or]] type `a`
  * @tparam b [[Or]] type `b`
  * @tparam rest the unused part of the input list
  */
abstract class OrInterpretation[
  a: Interpretation.withInOut[ain, aout]#t,
  b: Interpretation.withInOut[bin, bout]#t,
  ain <: List, aout,
  bin <: List, bout,
  rest <: List
] extends interpretation.Interpretation[Or[a, b]] {

  /**
    * The input type for an [[Or]] interpretation.
    */
  final type In = ain :: bin :: rest

}
