package fun
package typelevel
package or

import
  list.{
    List,
    ::
  },
  list.typelevel.{
    Concat
  },
  interpretation.{
    Interpretation
  }

/**
  * An interpretation of [[Or]].
  *
  * A runtime interpretation of [[Or]] accepts two instances, of types `a` and `b`,
  * and returns either `a` or `b`, depending on which type makes this [[Or]] evidence come true.
  * @tparam a [[Or]] type `a`
  * @tparam b [[Or]] type `b`
  * @tparam rest the unused part of the input list
  * @tparam ain
  * @tparam aout
  * @tparam bin
  * @tparam bout
  */
abstract class OrInterpretation[
  a: Interpretation.withInOut[ain, aout]#t,
  b: Interpretation.withInOut[bin, bout]#t,
  ain <: List, aout,
  bin <: List, bout,
  rest <: List
](
  implicit
  val abconcat: Concat[ain, bin]
) extends interpretation.Interpretation[Or[a, b]] {

  /**
    * The input type for an [[Or]] interpretation.
    */
  final type In = ain :: bin :: rest

}
