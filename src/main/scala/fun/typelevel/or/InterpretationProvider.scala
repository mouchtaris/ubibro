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
    Interpretation,
    Rest
  }

/**
  * Provide implicit interpreters for Or.
  *
  * This trait can be mixed into the [[Or]] companion object,
  * so that interpreters are provided implicitly without special
  * imports.
  */
trait InterpretationProvider {

  /**
    * Provide an interpretation of [[Or]], if it is true because of type `ora`.
    * @tparam a see [[OrInterpretationA]]
    * @tparam b see [[OrInterpretationA]]
    * @tparam rest see [[OrInterpretationA]]
    * @return an [[OrInterpretationA]]
    */
  @inline implicit def interpretationA[
    a: Or.resultOf[a, b]#t: Interpretation.withInOut[ain, aout]#t,
    b: Interpretation.withInOut[bin, bout]#t,
    ain <: List: Concat.to[bin]#t, aout,
    bin <: List, bout,
    rest <: List: Rest
  ]: OrInterpretationA[a, b, ain, aout, bin, bout, rest] =
    OrInterpretationA()

  /**
    * Provide an interpretation of [[Or]], if it is true because of type `orb`.
    * @tparam a see [[OrInterpretationB]]
    * @tparam b see [[OrInterpretationB]]
    * @tparam rest see [[OrInterpretationB]]
    * @return an [[OrInterpretationB]]
    */
  @inline implicit def interpretationB[
    a: Interpretation.withInOut[ain, aout]#t,
    b: Or.resultOf[a, b]#t: Interpretation.withInOut[bin, bout]#t,
    ain <: List: Concat.to[bin]#t, aout,
    bin <: List, bout,
    rest <: List: Rest
  ]: OrInterpretationB[a, b, ain, aout, bin, bout, rest] =
    OrInterpretationB()

}
