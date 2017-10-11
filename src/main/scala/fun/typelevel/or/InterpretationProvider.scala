package fun
package typelevel
package or

import
  list.{
    List,
  },
  list.typelevel.{
    Concat,
  },
  list.typelevel.concat.{
    ConcatInterpretation,
  },
  interpretation.{
    Interpretation,
    Rest,
  }

/**
  * Provide implicit interpreters for Or.
  *
  * This trait can be mixed into the [[Or]] companion object,
  * so that interpreters are provided implicitly without special
  * imports.
  */
trait InterpretationProvider {

  @inline implicit def interpretationA[
    a : Interpretation.withInOut[ain, aout]#t
      : Or.resultOf[a, b]#t,
    b : Interpretation.withInOut[bin, bout]#t,
    ain <: List, aout,
    bin <: List, bout,
    rest <: List: Rest,
    binrest <: List
      : Concat.resultOf[bin, rest]#t
      : ConcatInterpretation.inputOf[bin, rest]#t,
    in <: List
      : Concat.resultOf[ain, binrest]#t
      : ConcatInterpretation.inputOf[ain, binrest]#t
  ]: OrInterpretation[a, b, ain, aout, bin, bout, rest, aout, binrest, in] =
    OrInterpretation()

  @inline implicit def interpretationB[
    a : Interpretation.withInOut[ain, aout]#t,
    b : Interpretation.withInOut[bin, bout]#t
      : Or.resultOf[a, b]#t,
    ain <: List, aout,
    bin <: List, bout,
    rest <: List: Rest,
    binrest <: List
      : Concat.resultOf[bin, rest]#t
      : ConcatInterpretation.inputOf[bin, rest]#t,
    in <: List
      : Concat.resultOf[ain, binrest]#t
      : ConcatInterpretation.inputOf[ain, binrest]#t
  ]: OrInterpretation[a, b, ain, aout, bin, bout, rest, bout, binrest, in] =
    OrInterpretation()

}
