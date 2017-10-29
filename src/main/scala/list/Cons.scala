package list

private[list] final case class Cons[head, tail <: List](
  head: head,
  tail: tail
) extends (head :: tail)

