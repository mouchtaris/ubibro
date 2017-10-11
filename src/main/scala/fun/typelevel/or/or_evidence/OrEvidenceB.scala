package fun
package typelevel
package or
package or_evidence

/**
  * Evidence that `or` is true because of `b`
  */
protected[or] final case class OrEvidenceB[a, b, aout, bout]() extends AnyRef
  with OrEvidence[a, b, aout, bout]
{

  type Out = bout

  def apply(_aout: ⇒ aout)(bout: ⇒ bout): bout =
    bout

}

