package disjunction

trait ||[a, b] {

  type Out <: a | b

  val evidence: Out

}

object || {

  case class ||[a, b, out <: a | b](
    evidence: out
  ) extends disjunction.||[a, b] {
    type Out = out
  }

  implicit def orA[a, b](implicit a: a): ||[a, b, a] = ||(a)

  implicit def orB[a, b](implicit b: b): ||[a, b, b] = ||(b)

}

