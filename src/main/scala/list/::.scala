package list

trait ::[head, tail <: List] extends Any {

  val head: head

  val tail: tail

}

object :: {

  private[this] final case class ::[head, tail <: List](
    head: head,
    tail: tail
  ) extends list.::[head, tail] {

    @inline override lazy val toString: String =
      s"$head :: $tail"

  }

  @inline def apply[head, tail <: List](head: head, tail: tail): list.::[head, tail] =
    ::(head, tail)

}


