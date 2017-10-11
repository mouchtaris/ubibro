package fun
package typelevel
package or
package or_evidence

/**
  * Evidence that `or` is true because of `a`
  */
protected[or] final case class OrEvidenceA[a, b, aout, bout]() extends AnyRef
  with OrEvidence[a, b, aout, bout]
{

  type Out = aout

  def apply(aout: ⇒ aout)(_bout: ⇒ bout): aout =
    aout

}
