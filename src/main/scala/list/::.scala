package list

trait ::[head, tail <: List] extends Any {

  val head: head

  val tail: tail

  @inline final override lazy val toString: String =
    s"$head :: $tail"

}

object :: {

  @inline def apply[head, tail <: List](head: head, tail: tail): list.::[head, tail] =
    Cons(head, tail)

}


