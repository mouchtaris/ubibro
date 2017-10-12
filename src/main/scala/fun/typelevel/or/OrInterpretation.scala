package fun
package typelevel
package or

import
  list.{
    List,
    ::,
  },
  list.typelevel.{
    Concat,
  },
  list.typelevel.concat.{
    ConcatInterpretation,
  },
  interpretation.{
    Interpretation,
  }

final case class OrInterpretation[
  a,
  b,
  ain <: List, aout,
  bin <: List, bout,
  rest <: List,
  out
    : OrEvidence.resultOf[a, b, aout, bout]#t,
  binrest <: List
    : Concat.resultOf[bin, rest]#t
    : ConcatInterpretation.inputOf[bin, rest]#t,
  in <: List
    : Concat.resultOf[ain, binrest]#t
    : ConcatInterpretation.inputOf[ain, binrest]#t,
]()(
  implicit
  evidence: OrEvidence.Aux[a, b, aout, bout, out],
  interpa: Interpretation.Aux[a, ain, aout],
  interpb: Interpretation.Aux[b, bin, bout]
) extends Interpretation[Or[a, b]] {

  type In = in

  type Out = out

  val f: in ⇒ out = {
    case Concat(interpa(aout) :: Concat(interpb(bout) :: _ ) :: _) ⇒
      evidence(aout)(bout)
  }

}
