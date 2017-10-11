package fun
package typelevel
package or

import or_evidence.{
  OrEvidenceA,
  OrEvidenceB,
}

/**
  * Minimal required abstraction to implement the more complete one
  * of [[OrInterpretation]].
  * @tparam a or type `a`
  * @tparam b or type `b`
  * @tparam aout output type if `a` is true
  * @tparam bout output type if `b` is true
  */
trait OrEvidence[a, b, aout, bout] extends Any {

  /**
    * The type that makes [[Or]] true
    */
  type Out

  /**
    * Lazily evaluate at runtime an expression of `aout` or `bout`.
    */
  def apply(aout: ⇒ aout)(bout: ⇒ bout): Out

}

/**
  * Provide implicit resolution
  */
object OrEvidence {

  type Aux[a, b, aout, bout, out] = OrEvidence[a, b, aout, bout] {
    type Out = out
  }

  type resultOf[a, b, aout, bout] = {
    type t[out] = Aux[a, b, aout, bout, out]
  }

  @inline implicit def a[
    a: Or.resultOf[a, b]#t,
    b,
    aout,
    bout
  ]: OrEvidenceA[a, b, aout, bout] =
    OrEvidenceA()

  @inline implicit def b[
    a,
    b: Or.resultOf[a, b]#t,
    aout,
    bout
  ]: OrEvidenceB[a, b, aout, bout] =
    OrEvidenceB()

}
