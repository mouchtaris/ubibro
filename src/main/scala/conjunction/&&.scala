package conjunction

import
  Known._

final case class &&[a, b]()(
  implicit
  val a: a,
  val b: b
)

object && {

  @inline implicit def and[a: Known, b: Known]: a && b =
    &&()

}

